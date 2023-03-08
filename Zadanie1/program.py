import argparse

# Arguments for running the program
argParser = argparse.ArgumentParser()
argParser.add_argument("strategy", choices = ['bfs','dfs','astr'])
argParser.add_argument("parameter", choices = ['RDUL','RDLU','DRUL','DRLU','LUDR',
                                               'LURD','ULDR','ULRD', 'hamm','manh'])
argParser.add_argument("beginFile")
argParser.add_argument("endFile")
argParser.add_argument("extraFile")

args = argParser.parse_args()

def bfs_function():
    print("dupa")

def dfs_function():
     recursion_depth = 25
     temp_rec = 0
    #  if temp_rec == 25:
    #      exit()
     print(recursion_depth)

def astr_function():
     print("chuj")

match args.strategy:
    case "bfs":
        bfs_function()
    case "dfs":
        dfs_function()
    case "astr":
        astr_function()




