"""
Updated on April 26, 2021

@Author
Han Rui

@Usage
$python3.x Primers.py <i_int> <i_text> <i_str_1> <i_str_2> <<i_num_1>...<i_num_8>>
- <i_int>: strategy num
- <i_text>: readable text file. Seq and its SNP index interspersed by '\n'
- <i_str_1>: user account path
- <i_str_2>: filename of tag txt and xlsx is composed by username and time
- <<i_num_1>...<i_num_8>>: 8 num get from default settings

@Function
Containing 3 primer design strategies.
    Each strategy will return the best choice from design result according
    a series of params given by command line

@Note
barcode_96
    this var store the path of barcode file which containing
    96 (8bp) unique barcodes
"""

import os
import sys
import primer3
import pandas as pd

from itertools import combinations
from random import shuffle

strategy = eval(sys.argv[1])
seq_info = sys.argv[2]
f_recoder = os.path.join(sys.argv[3], sys.argv[4])
# Frozen value
bridge = "ATAGCGACGCGTTTCAAC"
barcode_96 = r"src/main/java/com/example/demo/tools/py/barcode_96.txt"

# Generate seq and index from file
seqArr = [i.strip() for i in open(seq_info) if set(i.strip().upper()) == {'A', 'C', 'G', 'T'}]
idxArr = [eval(i.strip()) for i in open(seq_info) if set(i.strip()).issubset(set('0123456789'))]

# Update list above by rm too short seq and its index
for i in range(len(seqArr)-1, -1, -1):
    if len(seqArr[i]) < 150 or idxArr[i] > len(seqArr[i]) or idxArr[i] < 0:
        seqArr.pop(i)
        idxArr.pop(i)

lenArr = list(map(int, sys.argv[5: 10]))
meltArr = list(map(float, sys.argv[10: 13]))


def designer_primer(target_seq, idx_snp, arr_len, arr_melt):
    """
    Designer for primers

    :param target_seq: Str
        template sequence for designing primers
    :param idx_snp: Int
        index of snp in target_seq
    :param arr_len: List
        containing lens of primers and PCR product
    :param arr_melt: List
        containing Tm value of primers
        
    :return: Dict
        containing all details of a best primer design strategy
    """
    seq_args = {
        # Identify the source of the chosen primers (str)
        'SEQUENCE_ID': 'user_seq',
        # Target sequence (5'->3') and it cannot span several lines (str)
        'SEQUENCE_TEMPLATE': target_seq,
        # Subset of template used to design primers ([int]; default: all)
        'SEQUENCE_INCLUDED_REGION': [0, len(target_seq)],
        # Internals that primer pairs must flank ([int]; default: none)
        'SEQUENCE_TARGET': [idx_snp, 0],
        # Interval of a paired primers (optional)
        # 'SEQUENCE_PRIMER_PAIR_OK_REGION_LIST': [[150, 150, 300, 300], [0, 300, 418, 182]],
    }
    global_args = {
        # ---Design Strategy
        # Max num of primer pairs to return and they are sorted by their "quality" (int; default: 5)
        'PRIMER_NUM_RETURN': 1,
        # Pick an internal primer (bool; default: 0)
        'PRIMER_PICK_INTERNAL_OLIGO': 0,

        # ---Length
        # Optimum length of primers (int; default: 20)
        'PRIMER_OPT_SIZE': arr_len[0],
        # Minimum length of primers (int; default: 18)
        'PRIMER_MIN_SIZE': arr_len[1],
        # Maximum length of primers (int; default: 25 (or 27))
        'PRIMER_MAX_SIZE': arr_len[2],
        # Product size range list ([int]; default: [180, 400] (or [100, 300]))
        'PRIMER_PRODUCT_SIZE_RANGE': [arr_len[3], arr_len[4]],

        # ---Tm of Primers (Celsius)
        # Optimum temperature of primers (float; default: 60.0)
        'PRIMER_OPT_TM': arr_melt[0],
        # Minimum temperature of primers (float; default: 50.0 (or 57.0))
        'PRIMER_MIN_TM': arr_melt[1],
        # Maximum temperature of primers (float; default: 65.0 (or 63.0))
        'PRIMER_MAX_TM': arr_melt[2],

        # ---GC Percent
        # Minimum GC percent of primers (float; default: 20.0)
        'PRIMER_MIN_GC': 20.0,
        # Maximum GC percent of primers (float; default: 80.0)
        'PRIMER_MAX_GC': 80.0,

        # ---Base Feature
        # Maximum repeat length (AAAAAA) of a mononucleotide  (int; default: 5)
        'PRIMER_MAX_POLY_X': 5,
        # Maximum number of unknown bases (N) allowable in any primer (int; default: 0)
        'PRIMER_MAX_NS_ACCEPTED': 0,
    }
    return primer3.bindings.designPrimers(seq_args, global_args)


def barcode_comb(num_seq, recoder):
    """
    Generate barcode pair combinations from barcode.txt

    :param num_seq: Int
        num of seq that user input fot design primers
    :param recoder: Str
        file for saving barcode combinations

    :return: List
        contain tuples of barcode pair combination
    """
    barcode = [i.strip() for i in open(barcode_96)]
    comb_all = list(combinations(barcode, 2))
    shuffle(comb_all)
    comb_pick = comb_all[: num_seq]
    # Save used combinations in file
    comb_recoder = open(recoder, 'w')
    for i in comb_pick:
        comb_recoder.write('\n'.join(i)+'\n')
    comb_recoder.close()
    return comb_pick


def attr_common(rst, prm_obj):
    """
    return common attributions of primer

    :param rst: List
        Nested list or matrix which len == 11
    :param prm_obj: primer3_obj
        return from primer3.bindings.designPrimers()

    :return: List
        Filled nest List except the first two sublist
    """
    # Primer position
    rst[2].append(prm_obj["PRIMER_LEFT_0"][0])
    rst[3].append(prm_obj["PRIMER_LEFT_0"][1])
    rst[4].append(prm_obj["PRIMER_RIGHT_0"][0])
    rst[5].append(prm_obj["PRIMER_RIGHT_0"][1])
    # Primer Tm value
    rst[6].append(round(prm_obj["PRIMER_LEFT_0_TM"], 2))
    rst[7].append(round(prm_obj["PRIMER_RIGHT_0_TM"], 2))
    # Primer GC percent
    rst[8].append(prm_obj["PRIMER_LEFT_0_GC_PERCENT"])
    rst[9].append(prm_obj["PRIMER_RIGHT_0_GC_PERCENT"])
    # Product size
    rst[10].append(prm_obj["PRIMER_PAIR_0_PRODUCT_SIZE"])


def data_frame(attr_sub):
    """
    build dataframe for xlsx file

    :param attr_sub: List
        Nested list or matrix which len == 11

    :return: Dataframe
    """
    return pd.DataFrame({
        'Num': list(range(1, len(seqArr)+1)),
        'Product Size': attr_sub[10],
        'Left Barcode Primer Seq': attr_sub[0],
        'Right Barcode Primer Seq': attr_sub[1],
        'Left Primer Tm': attr_sub[6],
        'Right Primer Tm': attr_sub[7],
        'Left Primer GC': attr_sub[8],
        'Right Primer GC': attr_sub[9],
        'Left Primer 5`-pos': attr_sub[2],
        'Right Primer 5`-pos': attr_sub[4],
        'Left Primer Length': attr_sub[3],
        'Right Primer Length': attr_sub[5],
    })


# Strategy 1: add unique barcode combinations to left and right primer
if strategy == 1 and len(seqArr) <= 4560:
    result = [[] for _ in range(11)]
    comb = barcode_comb(len(seqArr), f_recoder+'.txt')
    for i in range(len(seqArr)):
        prmRst = designer_primer(seqArr[i], idxArr[i], lenArr, meltArr)
        # Barcode + Primer sequence
        result[0].append(comb[i][0]+prmRst["PRIMER_LEFT_0_SEQUENCE"])
        result[1].append(comb[i][1]+prmRst["PRIMER_RIGHT_0_SEQUENCE"])
        attr_common(result, prmRst)
    dataframe = data_frame(result)
    dataframe.to_excel(f_recoder+'.xlsx', index=False)

# Strategy 2: add unique barcode combinations ligated by bridge seq to right primer
if strategy == 2 and len(seqArr) <= 4560:
    result = [[] for _ in range(11)]
    comb = barcode_comb(len(seqArr), f_recoder+'.txt')
    for i in range(len(seqArr)):
        prmRst = designer_primer(seqArr[i], idxArr[i], lenArr, meltArr)
        # Barcode + bridge + Primer sequence
        result[0].append(prmRst["PRIMER_LEFT_0_SEQUENCE"])
        result[1].append(
            comb[i][0]+bridge+comb[i][1]+prmRst["PRIMER_RIGHT_0_SEQUENCE"]
        )
        attr_common(result, prmRst)
    dataframe = data_frame(result)
    dataframe.to_excel(f_recoder + '.xlsx', index=False)

# Strategy 3: add UMI and unique barcode seq to right primer
if strategy == 3 and len(seqArr) <= 96:
    result = [[] for _ in range(11)]
    # Select unique barcode randomly
    uniqTag = [i.strip() for i in open(barcode_96)]
    shuffle(uniqTag)
    # Save unique barcode
    uniqRecoder = open(f_recoder+'.txt', 'w')
    uniqRecoder.write('\n'.join(uniqTag[: len(seqArr)]))
    uniqRecoder.close()
    for i in range(len(seqArr)):
        prmRst = designer_primer(seqArr[i], idxArr[i], lenArr, meltArr)
        # Barcode + UMI + Primer sequence
        result[0].append(prmRst["PRIMER_LEFT_0_SEQUENCE"])
        result[1].append(uniqTag[i] + "NNNNNN" + prmRst["PRIMER_RIGHT_0_SEQUENCE"])
        attr_common(result, prmRst)
    dataframe = data_frame(result)
    dataframe.to_excel(f_recoder + '.xlsx', index=False)
