"""
Created on April 22, 2021

@Author
Han Rui

@Usage
$python3.x Minima.py <i_csv> <o_dir/xlsx> <o_dir/png>
- <i_csv>: two cols: in order of sample num and base freq
- <o_dir/xlsx>: dir for saving genotype result (Need to be created before use)
- <o_dir/png>: dir for saving KDE plot (Need to be created before use)

@Function
Plot KDE curve with minima dots.
    the KDE bandwidth is found by LOO of Cross Check.

Output genotype for each sample.

@Note
o_format = ["pdf", "xlsx"]
    The list which contains 2 suffix strings controlling format of output.
    The first suffix should be a kind of graph (such as png, jpg, svg, etc.).
    The second suffix has associated with a function which means other suffix
    may not work.
"""

import os
import sys
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

from sklearn.neighbors import KernelDensity
from sklearn.model_selection import GridSearchCV
from sklearn.model_selection import LeaveOneOut

i_csv = sys.argv[1]
fileName = os.path.split(i_csv)[1].split('.')[0]
# Dirs for saving results
o_dir_xlsx = sys.argv[2]
o_dir_png = sys.argv[3]

# Read as a row vector
freq = pd.read_csv(i_csv, header=0, names=["Num", "Frequency"])["Frequency"]
freq_arr = np.array(freq)

# Cross-validation for best bandwidth of KDE
grid = GridSearchCV(
    estimator=KernelDensity(kernel='gaussian'),
    param_grid={'bandwidth': 10 ** np.linspace(-1.2, -0.3, 100)},
    cv=LeaveOneOut(),
)
grid.fit(freq_arr.reshape(-1, 1))
bandwidth_best = grid.best_params_["bandwidth"]

# Build suitable KDE model
model = KernelDensity(bandwidth=bandwidth_best, kernel='gaussian')
model.fit(freq_arr.reshape(-1, 1))
# Pick 1000 dots evenly in x-axis
freq_range = np.linspace(freq.min() - 1, freq.max() + 1, 1000)
# Estimate prob in y-axis for given dots
freq_log_prob = model.score_samples(freq_range.reshape(-1, 1))
freq_prob = np.exp(freq_log_prob)

# Pick minima coordinates from the KDE curve
xMini = []
yMini = []
for i in range(1, len(freq_range) - 1):
    if freq_prob[i - 1] > freq_prob[i] and freq_prob[i + 1] > freq_prob[i] \
            and 0 <= freq_range[i] <= 1:
        xMini.append(freq_range[i])
        yMini.append(freq_prob[i])

# Plot
plt.rc('font', family='SimHei')
plt.title(fileName)
plt.xlim(0, 1)
sns.set()
sns.despine(top=True, right=False, offset=5)
# Rug
sns.distplot(
    freq_arr, kde=False,
    rug=True,
    rug_kws={
        'color': '#2978b5', 'alpha': 0.5,
    },
    hist=False, norm_hist=True,
    hist_kws={
        'color': '#8BB0A6', 'alpha': 0.25,
    },
)
# KDE curve
plt.plot(freq_range, freq_prob, color='#2978b5', label='KDE')
plt.ylim(ymin=0)
plt.fill(freq_range, freq_prob, color='#fbe0c4', alpha=0.5)
# Minima dots
plt.scatter(x=xMini, y=yMini, s=16, c='#0061a8', label='minima')
plt.legend()
# Text
for i in range(len(xMini)):
    plt.text(
        xMini[i], yMini[i],
        str(round(xMini[i], 3))+"\n",
        ha="center", va="bottom"
    )
# Pdf
plt.savefig(
    os.path.join(
        o_dir_png, fileName+".png"
    ),
    format='png',
    dpi=600,
)
plt.close()

# Genotype
genotype = []
xZone = [0] + xMini + [1]
for i in freq:
    n = 0
    for j in range(len(xZone)-1):
        if xZone[j] <= i <= xZone[j+1]:
            genotype.append(n)
        n += 1
dataFrame = pd.DataFrame({
    'Num': pd.read_csv(i_csv, header=0, names=["Num", "Frequency"])["Num"],
    'Frequency': freq,
    'Genotype': genotype,
})
# Xlsx
dataFrame.to_excel(
    os.path.join(
        o_dir_xlsx, fileName+".xlsx"
    ),
    index=False,
)
