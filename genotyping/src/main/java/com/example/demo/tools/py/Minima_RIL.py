"""
Updated on March 10, 2021

@Author
Han Rui

@Usage
$python3.x Minima_RIL.py <inputFile>.csv <outputDir_xlsx> <outputDir_png>

@Function
Distinguish (default cluster=3) different genotypes by performing kernel
density estimation (KDE) using the frequency of the input bases. The result
will be build as a new column to the end of the original input in a new csv
(in <outputDir_xlsx>). The output image (in <outputDir_png>) may helps you
adjust parameters to achieve the best results.

@Note
The only input file (<inputFile>.csv) must named as <chr>_<position>_<base>.csv
and be in a format containing two columns of data, the first column of data is
the sample number and the second column of data is the percentage of base.
"""

import sys
import os
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import statsmodels.nonparametric.api as sm

csvPath = sys.argv[1]
xlsxDir = sys.argv[2]
graphDir = sys.argv[3]


def rmMaxVal(xArray, yArray, num):
    """
    This method is called recursively, sorting the list and remove the maximum
    point of the vertical coordinate from the two input lists.

    :param xArray: (List)
        Some horizontal coordinates including minima.
    :param yArray: (List)
        Some vertical coordinates including minima.
    :param num: (Integer)
        Equal to the number of cluster, which also represents the number of
        genotypes that need to be distinguished.

    :return: (List)
        The length of the list is equal to the number of clusters after
        recursively remove.
    """
    index = yArray.index(max(yArray))
    xArray.pop(index)
    yArray.pop(index)
    if len(xArray) == num-1:
        return xArray, yArray
    return rmMaxVal(xArray, yArray, num)


freq = pd.read_csv(csvPath, header=0, names=["Num", "Frequency"])["Frequency"]
# Get the discrete coordinates of the points on the KDE curve
curve = sm.KDEUnivariate(freq)
curve.fit(kernel="gau", bw="scott", fft=True, gridsize=10000, cut=3)
xList, yList = curve.support, curve.density

xMinList = []
yMinList = []
# Find minimal points and pick lower vertical coordinates
for i in range(1, len(xList)-1):
    if 0 <= xList[i] <= 1 and yList[i-1] > yList[i] and yList[i+1] > yList[i]:
        xMinList.append(xList[i])
        yMinList.append(yList[i])
cluster = 3
"""
This parameter means that how many genotypes are distinguished. If you change 
the value, please also change the number of 'clusterName' list elements 
representing the genotype name
"""
if len(xMinList) > cluster-1 > 0:
    xMinList, yMinList = rmMaxVal(xMinList, yMinList, cluster)

# Plot KDE curve and save as png
for i in range(len(xMinList)):
    plt.text(xMinList[i]-0.039, yMinList[i]+max(yList)*0.618**6,
             "%.3f" % (xMinList[i]))
sns.set(style="white")
sns.kdeplot(freq, shade=True, color='g', label='KDE')
sns.scatterplot(x=xMinList, y=yMinList, label="minima")
sns.despine(top=True, right=False, offset=5)
plt.xlim(0, 1)
plt.xticks(np.arange(0, 1.001, 0.25))
plt.ylim(ymin=0)
plt.ylabel("Density")
# Name title and axis of plot by csv file name
csvName = csvPath.split("/")[-1].split('.')[0]
csvNameList = csvName.split('_')
plt.xlabel("Frequency of "+csvNameList[2])
plt.title("chr"+csvNameList[0]+": "+csvNameList[1], fontdict={'size': 15})
plt.savefig(graphDir+csvName+".png")
plt.figure(figsize=(6,3),dpi=200)

# Distinguish the three genotypes and save as csv
genotype = []
clusterName = ['2', '0']
"""
The length of the list is recommended to be consistent with the 'cluster' value
Its String represent, from left to right, the genotypes that were distinguished
"""
xMinList.insert(0, 0)
xMinList.append(1)
for i in freq:
    for j in range(len(xMinList)-1):
        if xMinList[j] <= i <= xMinList[j+1]:
            genotype.append(clusterName[j])
dataFrame = pd.DataFrame({'Num': [i for i in range(len(freq))],
                          'Frequency': freq,
                          'Genotype': genotype})
dataFrame.to_excel(xlsxDir+csvName+".xlsx", index=False)
