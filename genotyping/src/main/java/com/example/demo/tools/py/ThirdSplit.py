import sys

tempFileName = sys.argv[4]

a = []
b = []
c = []
dic = {}
n = 1
f = open(sys.argv[1], 'r')
file = open(sys.argv[2], 'r')
file1 = open(sys.argv[3], 'r')

for i in f.readlines():
    a.append(i.strip())
f.close()

for i in file.readlines():
    b.append(i)
    if n % 4 == 0:
        dic[b[0]] = b
        b = []
    n += 1

for e in range(96):
    n = 1
    file2 = open("src/main/resources/"+tempFileName+"/fqFiles/fq/" + str(e) + "_1.fq", 'w')
    file3 = open("src/main/resources/"+tempFileName+"/fqFiles/fq/" + str(e) + "_2.fq", 'w')
    file1.seek(0)
    for i in file1.readlines():
        c.append(i)
        if n % 4 == 0:
            num = c[0].replace("/2", "/1")
            if (a[e] == c[1][0:8]) or (a[e] == dic[num][1][0:8]):
                file2.write(''.join(dic[num]))
                file3.write(''.join(c))
            c = []
        n += 1
file.close()
file1.close()
