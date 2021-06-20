import sys
import primer3

tempSeq = sys.argv[1]
snpIdx = eval(sys.argv[2])
lengthArr = list(map(int, sys.argv[3: 8]))
meltArr = list(map(float, sys.argv[8: 11]))

# Input sequence must achieve aimed length (300+1+300)
if snpIdx > 300 and len(tempSeq) - (snpIdx + 1) > 300:
    targetSeq = tempSeq[snpIdx - 300: snpIdx + 301]
    seq_args = {
        # Identify the source of the chosen primers (str)
        'SEQUENCE_ID': 'tmp_seq',
        # Target sequence (5'->3') and it cannot span several lines (str)
        'SEQUENCE_TEMPLATE': targetSeq,
        # Subset of template used to design primers ([int]; default: all)
        'SEQUENCE_INCLUDED_REGION': [0, len(targetSeq)],
        # Internals that primer pairs must flank ([int]; default: none)
        'SEQUENCE_TARGET': [300, 0],
        # Interval of a paired primers (optional)
        'SEQUENCE_PRIMER_PAIR_OK_REGION_LIST': [[150, 150, 300, 300], [0, 300, 418, 182]],
    }
    global_args = {
        # ---Design Strategy
        # Max num of primer pairs to return and they are sorted by their "quality" (int; default: 5)
        'PRIMER_NUM_RETURN': 5,
        # Pick an internal primer (bool; default: 0)
        'PRIMER_PICK_INTERNAL_OLIGO': 0,

        # ---Length
        # Optimum length of primers (int; default: 20)
        'PRIMER_OPT_SIZE': lengthArr[0],
        # Minimum length of primers (int; default: 18)
        'PRIMER_MIN_SIZE': lengthArr[1],
        # Maximum length of primers (int; default: 25 (or 27))
        'PRIMER_MAX_SIZE': lengthArr[2],
        # Product size range list ([int]; default: [180, 400] (or [100, 300]))
        'PRIMER_PRODUCT_SIZE_RANGE': [lengthArr[3], lengthArr[4]],

        # ---Tm of Primers (Celsius)
        # Optimum temperature of primers (float; default: 60.0)
        'PRIMER_OPT_TM': meltArr[0],
        # Minimum temperature of primers (float; default: 50.0 (or 57.0))
        'PRIMER_MIN_TM': meltArr[1],
        # Maximum temperature of primers (float; default: 65.0 (or 63.0))
        'PRIMER_MAX_TM': meltArr[2],

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
    primRst = primer3.bindings.designPrimers(seq_args, global_args)
    fo = open("src\\main\\resources\\primerDesign\\primer.txt", "w")


    for i in range(global_args['PRIMER_NUM_RETURN']):
#         fo.write("---Primer" + str(i) + "---"+"\n")
        print("---Primer" + str(i) + "---")

        fo.write(str(primRst["PRIMER_PAIR_" + str(i) + "_PENALTY"]))
        fo.write("\n")
        fo.write(str(primRst["PRIMER_LEFT_" + str(i) + "_PENALTY"]))
        fo.write("\n")
        fo.write(str(primRst["PRIMER_RIGHT_" + str(i) + "_PENALTY"]))
        fo.write("\n")
        print(primRst["PRIMER_PAIR_" + str(i) + "_PENALTY"])
        print(primRst["PRIMER_LEFT_" + str(i) + "_PENALTY"])
        print(primRst["PRIMER_RIGHT_" + str(i) + "_PENALTY"])

        fo.write(primRst["PRIMER_LEFT_" + str(i) + "_SEQUENCE"])
        fo.write("\n")
        fo.write(primRst["PRIMER_RIGHT_" + str(i) + "_SEQUENCE"])
        fo.write("\n")
        print(primRst["PRIMER_LEFT_" + str(i) + "_SEQUENCE"])
        print(primRst["PRIMER_RIGHT_" + str(i) + "_SEQUENCE"])

        fo.write(str(primRst["PRIMER_LEFT_" + str(i)][0]))
        fo.write("\n")
        fo.write(str(primRst["PRIMER_LEFT_" + str(i)][1]))
        fo.write("\n")
        fo.write(str(primRst["PRIMER_RIGHT_" + str(i)][0]))
        fo.write("\n")
        fo.write(str(primRst["PRIMER_RIGHT_" + str(i)][1]))
        fo.write("\n")
        
        print(primRst["PRIMER_LEFT_" + str(i)][0])
        print(primRst["PRIMER_LEFT_" + str(i)][1])
        print(primRst["PRIMER_RIGHT_" + str(i)][0])
        print(primRst["PRIMER_RIGHT_" + str(i)][1])

        fo.write(str(primRst["PRIMER_LEFT_" + str(i) + "_TM"]))
        fo.write("\n")
        fo.write(str(primRst["PRIMER_RIGHT_" + str(i) + "_TM"]))
        fo.write("\n")
        print(primRst["PRIMER_LEFT_" + str(i) + "_TM"])
        print(primRst["PRIMER_RIGHT_" + str(i) + "_TM"])

        fo.write(str(primRst["PRIMER_LEFT_" + str(i) + "_GC_PERCENT"]))
        fo.write("\n")
        fo.write(str(primRst["PRIMER_RIGHT_" + str(i) + "_GC_PERCENT"]))
        fo.write("\n")
        print(primRst["PRIMER_LEFT_" + str(i) + "_GC_PERCENT"])
        print(primRst["PRIMER_RIGHT_" + str(i) + "_GC_PERCENT"])

        fo.write(str(primRst["PRIMER_PAIR_" + str(i) + "_PRODUCT_SIZE"]))
        fo.write("\n")
        print(primRst["PRIMER_PAIR_" + str(i) + "_PRODUCT_SIZE"])

    fo.close()
