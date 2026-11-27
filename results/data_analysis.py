import statistics

def main():
    PATHS = [
        "./sudoku_dpll.csv",
        "./sudoku_kissat.csv",
        "./dpll-reset/uf20-91.csv",
        "./dpll-reset/uf50-218.csv",
        "./dpll-reset/uf100-430.csv",
        "./dpll-reset/uf125-538.csv",
        "./dpll-reset/uf150-645.csv",
        "./kissat/uf20-91.csv",
        "./kissat/uf50-218.csv",
        "./kissat/uf100-430.csv",
        "./kissat/uf125-538.csv",
        "./kissat/uf150-645.csv",
        "./kissat/uf175-753.csv"
    ]
    for path in PATHS:
        time_taken = []
        with open(path, 'r') as f:
            for line in f:
                time_taken.append(float(line.strip().split()[1])/1e9)
        print(f"file: {path}")
        print(f"time taken mean: {statistics.mean(time_taken)}s")
        print(f"time taken std dev: {statistics.stdev(time_taken)}s")
        print()

if __name__ == "__main__":
    main()
