"""
Created on March 3, 2021

@Author
Han Rui

@Usage
$python3.x FilterSNP.py <inputFile>.csv <inputDir_wig> <outputDir_csv>

@Function
For each input SNP and base (from <inputFile>.csv which arranged by chromosome
number, locus, and base in order and with a line header), calculate the
frequency of corresponding base in each wig file (<inputDir_wig>) and output as
csv (<outputDir_csv>).

@Note
Do not use np.loadtxt() in server! if you do not sure it will work.
"""

import os
import sys
import numpy as np
import pandas as pd

snpCsv = sys.argv[1]
wigDir = sys.argv[2]
csvDir = sys.argv[3]

def wigDict(wig_Path):
    """
    Build a dict by chromosome for each wig.

    :param wig_Path: (Str)
        A path string of wig file.

    :return: (Dict)
        A dictionary which key is chromosome num and value is wig num matrix of
        corresponding chromosome
    """
    wig_file = open(wig_Path, 'r')
    chr_dict = {}
    chr_num = ''
    row = 1
    for m in wig_file:
        if row > 2:
            # Determine the comment line
            if m[:12] == "variableStep":
                chr_num = m.split()[1].split("=")[1]
                chr_dict[chr_num] = []
                continue
            chr_dict[chr_num].append(m)
        row += 1
    return chr_dict


# Get names of all files in the directory
wigList = []
for triTuple in os.walk(wigDir):
    wigList = triTuple[2]

baseMap = {'A': 0, 'C': 1, 'G': 2, 'T': 3}
# Process each loci
snpMat = np.loadtxt(snpCsv, skiprows=1, usecols=(0, 1, 2), dtype=str, delimiter=',')
for i in range(np.shape(snpMat)[0]):
    freqList = []
    sampList = []
    # Traversing the directory
    for j in wigList:
        wigPath = os.path.join(wigDir, j)
        chrDict = wigDict(wigPath)

        if snpMat[i][0] not in chrDict.keys():
            continue

        # Save first column's nums in a list
        posArr = []
        for k in chrDict[snpMat[i][0]]:
            posArr.append(k.split("\t", 1)[0])
        if snpMat[i][1] not in posArr:
            continue

        # Find pileArr in pileMat
        arrInd = posArr.index(snpMat[i][1])
        pileList = chrDict[snpMat[i][0]][arrInd].split('\t', 5)[1: 5]
        pileArr = [eval(x) for x in pileList]
        if sum(pileArr) > 50:
            """
            This 50 is a value given based on experience and
            can be optimized later
            """
            # Calculate the base frequency
            freq = pileArr[baseMap[snpMat[i][2]]] / sum(pileArr)
            freqList.append(freq)
            sampList.append(j.split('.')[0])
    # Build dataframe and save as csv
    dataFrame = pd.DataFrame({"Num": sampList, 'Frequency': freqList})
    dataFrame.to_csv(csvDir + "_".join(snpMat[i]) + ".csv", index=False, sep=',')
