import statistics
import os
import sys
import math
import matplotlib.pyplot as plt
import matplotlib.colors as colors
import scipy.stats as stats
import statsmodels.api as sm
import numpy as np
import itertools
import random
from color_map import color_map


def file_summary(filepath):
    with open(filepath) as file:
        lines = file.readlines()
        setup_times = [int(x) for x in lines[0].split(',')]
        experiment_times = [int(x) for x in lines[1].split(',')]
        
        return setup_times, experiment_times


def folder_summary(folderpath, x_min, x_max, color, mode='experiment'):
    print(folderpath)
    xs = [2 ** exp for exp in range(x_min, x_max + 1)]
    # xs = [exp for exp in range(x_min, x_max + 1)]
    lower_ys = []
    ys = []
    all_ys = []
    upper_ys = []

    all_experiments = [[] for _ in range(len(xs))]
    all_setup = [[] for _ in range(len(xs))]



    for root, dirs, files in os.walk(folderpath):
        for file in files:
            index = int(file.split('-')[1]) - x_min
            setup_times, experiment_times = file_summary(folderpath + "/" + file)

            all_experiments[index].extend(experiment_times)
            all_setup[index].extend(setup_times)


            # ys.append(summary["experiment_mean"])
            # print(f'{file : <30} - {file_summary(folderpath + "/" + file)["experiment_mean"]}')
            # fit = stats.t.fit(summary["experiment_times"])
            # ci = stats.t.interval(0.95, *fit)
            # lower_ys.append(ci[0])
            # upper_ys.append(ci[1])
            # print(f'{file : <30} - ci={ci}')

    values = all_experiments if mode == 'experiment' else all_setup
    # print(values)
    for exponent, times in enumerate(values):
    # for exponent, times in enumerate(all_setup):
        fit = stats.t.fit(times)
        ci = stats.t.interval(0.95, *fit)
        print(ci)
        all_ys.append(times)
        ys.append(statistics.mean(times))
        lower_ys.append(ci[0])
        upper_ys.append(ci[1])
        # plt.scatter([2 ** (exponent + x_min) for _ in times], times)

    return all_ys

    # return lower_ys, ys, upper_ys

    # ys.extend([0] * (len(xs) - len(ys)))
    # plt.xticks([2**exp for exp in range(X_LIM)], [str(2**exp) for exp in range(X_LIM)])

    style = 'solid' if 'uniform' in folderpath else 'dashed'
    style = 'solid'
    # style = 'solid' if mode == 'experiment' else 'dotted'
    # plt.boxplot(all_ys, positions=xs, notch=False, widths=100000, showfliers=False)
    # plt.boxplot(all_ys, positions=xs, notch=False, showfliers=False, boxprops=dict(color=color))
    plt.plot(xs, ys, color=color, linestyle=style, alpha=0.75)

    # lower_ys = ys - np.array([np.quantile(y, 0.25) for y in all_ys])
    # upper_ys = np.array([np.quantile(y, 0.75) for y in all_ys]) - ys
    # print(lower_ys)
    # print(ys)
    # print(upper_ys)
    # plt.errorbar(xs, ys, yerr=[lower_ys, upper_ys], uplims=upper_ys, lolims=lower_ys, color=color, linewidth=2, alpha=0.75)

    plt.scatter(xs, ys, color=color)
    plt.fill_between(xs, lower_ys, upper_ys, alpha=0.25, color=color)

    # plt.hist(list(itertools.chain.from_iterable(values)))
    # plt.show()

if __name__ == '__main__':
    plt.style.use('mine.mplstyle')
    plt.figure(figsize=(2, 1))
    # colors = list(colors.get_named_colors_mapping().keys())
    # random.shuffle(colors)
    # colors = ['black', 'red', 'darkorange', 'olivedrab', 'lime', 'turquoise', 'deepskyblue', 'navy', 'magenta', 'royalblue', 'forestgreen', 'r', 'purple']

    # colors = ['gold', 'royalblue', 'forestgreen', 'r', 'purple', 'gold', 'royalblue', 'forestgreen', 'r', 'purple']
    # colors = ['red', 'yellow', 'blue', 'green']
    yss = []
    for i, arg in enumerate(sys.argv[1:]):
        name = arg.split('/')[1]
        # folder_summary(arg, 10, 20, colors[i], 'experiment')
        # folder_summary(arg, 10, 20, colors[i], 'setup')

        folder_summary(arg, 10, 20, color_map[name], 'experiment')
        # folder_summary(arg, 10, 20, color_map[name], 'setup')

        # yss.append(ys)


    # plt.  ([label[20: -1] for label in sys.argv[1:]])
    names = [arg.split('/')[1] for arg in sys.argv[1:]]
    # for i in range(len(names)):
    #     suffix = ' uniform' if i <= 1 else ' normal'
    #     names[i] = names[i] + suffix
    plt.legend(names, fontsize='32', loc='upper left')
    

    # plt.xlabel('$e$ where $2^e=n$')
    plt.xlabel('$n$')
    plt.ylabel('Execution Time (ms)')
    # plt.ylim((0, 200))
    # plt.title('Setup time of PHT on normally distributed data')
    plt.grid(True)
    plt.show()

    # s = file_summary('sparsity-large/binarytrie/u-16')
    # times = s['experiment_times']
    # mean = s['experiment_mean']
    # sd = s['experiment_sd']

    # times = [(t - min(times)) / (max(times) - min(times)) for t in times]

    # fig = sm.qqplot(np.array(times), line='45')
    # plt.show()
    # plt.scatter(list(range(len(times))), times)
    # plt.grid()
    # plt.show()

    # plt.scatter(list(range(99)), sorted(s['experiment_times']))
    # plt.show()