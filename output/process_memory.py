import matplotlib.pyplot as plt
import numpy as np
import statistics
import scipy.stats as stats
from color_map import color_map


def getFileList(filepath):
    with open(filepath) as file:
        return [[int(x) for x in line.split(',')] for line in file.readlines()]
        

if __name__ == '__main__':
    plt.style.use('mine.mplstyle')
    xs = [2 ** x for x in range(10, 21)]

    # names = ['binarytrie', 'redblacktree', 'splaytree', 'bitmap', 'linkedlist']
    # names = ['binarytrie', 'binaryptrie', 'hashtable', 
    # 'redblacktree', 'splaytree', 'sib', 'bitmap', 'vebtree', 'vebbitmap']
    names = ['ChainedDivisionHashTable', 'Bitmap', 'vEBTree', 'RedBlackTree', 'BinaryTrie']
    legend_names = ['HashTable', 'Bitmap', 'vEBTree', 'RedBlackTree', 'BinaryTrie']

    # names = ['vEBBitmap', 'vEBTree', 'vEBBitmapMin']
    # names = ['PerfectHashTable', 'ChainedDivisionHashTable', 'ABHashTable']
    filenames = ['family-uniform-memory/' + name for name in names]
    # filenames.extend(['veb-variant-memory-uniform/' + name for name in names])
    # colors = ['orange', 'royalblue', 'lime'] * 2
    colors = [color_map[name] for name in names]

    for i, filename in enumerate(filenames):
        lists = getFileList(filename)
        avgs = [statistics.mean(l) for l in lists]

        lower_bounds = []
        upper_bounds = []
        
       
        linestyle = 'solid'
        linestyle = 'solid' if 'normal' in filename else 'dashed'
        marker = 'o'

        for l in lists:
            fit = stats.norm.fit(l)
            ci = stats.norm.interval(0.95, *fit)
            lower_bounds.append(ci[0])
            upper_bounds.append(ci[1])


        # print(lower_bounds)
        # print(upper_bounds)

        # lower_bounds = np.array(avgs) - np.array(lower_bounds)
        # upper_bounds = np.array(upper_bounds) - np.array(avgs)
        # plt.errorbar(xs, avgs, yerr=[lower_bounds, upper_bounds],  uplims=lower_bounds, lolims=upper_bounds, color=colors[i], linestyle=linestyle, alpha=0.5)

        plt.plot(xs, avgs, color=colors[i], linestyle=linestyle, alpha=0.75)
        plt.scatter(xs, avgs, color=colors[i], marker=marker, alpha=0.75)
        plt.fill_between(xs, lower_bounds, upper_bounds, alpha=0.33, color=colors[i])

    # names = ['vEBBitmap normal', 'vEBTree normal', 'vEBBitmapMin normal', 'vEBBitmap uniform', 'vEBTree uniform', 'vEBBitmapMin uniform']
    plt.legend(legend_names, fontsize='30', loc='center left')
    plt.xlabel('n')
    plt.ylabel('Memory Usage (#bytes)')
    # plt.title('Memory usage as $n$ increases on normally distributed data')
    # plt.title('Memory usage of hash tables on normally distributed data.')
    plt.grid()
    plt.figure(figsize=(2, 1))
    plt.show()
    