#-*- coding: UTF-8 -*-
import xlrd
import xlsxwriter
import glob
import os
import sys
biao_tou = "NULL"
wei_zhi = "NULL"

# 打开Exce文件
def open_exce(name):
    print("called open_workbook")
    fh = xlrd.open_workbook(name)
    print("open Excel")
    return fh

# 获取exce文件下的所有sheet


def get_sheet(fh):
    sheets = fh.sheets()
    print("get all sheet")
    return sheets

# 获取sheet下有多少行数据


def get_sheetrow_num(sheet):
    # print("获取sheet下有多少行数据")
    print("get the number of lines")
    return sheet.nrows

# 获取sheet下的数据


def get_sheet_data(all_data1, sheet, row):
    # print("获取sheet下的数据")
    print("get the data of sheet")
    for i in range(row):
        if (i == 0):
            global biao_tou
            biao_tou = sheet.row_values(i)
            continue
        values = sheet.row_values(i)
        all_data1.append(values)

    return all_data1

# 获取要合并的所有exce表格


def get_exce(path):
    # print("获取要合并的所有exce表格")
    print("merge all excels")
    global wei_zhi
    wei_zhi = path
    all_exce = glob.glob(wei_zhi + "*.xlsx")
    # print("该目录下有" + str(len(all_exce)) + "个exce文件：")
    if(len(all_exce) == 0):
        return 0
    else:
        for i in range(len(all_exce)):
            print(all_exce[i])
        return all_exce


def start(Path):
    path = Path  # 'D:/ljx/output/xlsx/'
    all_exce = get_exce(path)
    # 得到要合并的所有exce表格数据
    if(all_exce == 0):
        print("there is no xlsx'file")
        os.system('pause')
        exit()

    all_data1 = []
    # 用于保存合并的所有行的数据
    hang = 0
    # 下面开始文件数据的获取
    for exce in all_exce:
        # 打开文件
        fh = open_exce(exce)

        # 获取文件下的sheet数量
        sheets = get_sheet(fh)

        for sheet in range(len(sheets)):
            row = get_sheetrow_num(sheets[sheet])
            # 获取一个sheet下的所有的数据的行数

            all_data2 = get_sheet_data(all_data1, sheets[sheet], row)
            # 获取一个sheet下的所有行的数据
            all_data2.insert(hang, " ")
            hang = hang+1
            all_data2.insert(hang, biao_tou)
            # 写入表头
            hang = hang+row

    # all_data2.insert(0, biao_tou)
    # 表头写入

    # 下面开始文件数据的写入
    # print("下面开始文件数据的写入")
    print("start to write data")
    new_exce = wei_zhi + "mergedExcel.xlsx"
    # 新建的exce文件名字

    fh1 = xlsxwriter.Workbook(new_exce)
    # 新建一个exce表

    new_sheet = fh1.add_worksheet()
    # 新建一个sheet表

    for i in range(len(all_data2)):

        for j in range(len(all_data2[i])):
            c = all_data2[i][j]
            new_sheet.write(i, j, c)

    fh1.close()
    # 关闭该exce表

    print(wei_zhi)


if __name__ == "__main__":
    path=sys.argv[1]
    print("variable:"+path)
    start(path)

