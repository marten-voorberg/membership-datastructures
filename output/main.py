from process import *
from process_memory import *
from color_map import *
import matplotlib.pyplot as plt
import statistics
import matplotlib 


# if __name__ == '__main__':
#     plt.style.use('mine.mplstyle')

#     names = ['RedBlackTree', 'BinaryTrie', 'vEBTree', 'ChainedDivisionHashTable', 'Bitmap']
#     folder = 'dynamic-5'

#     fig, ax1 = plt.subplots()
#     ax1.set_xlabel('$n$', fontsize='xx-large')
#     ax1.set_ylabel('Execution time (ms, solid)', fontsize='xx-large')
#     ax1.set_ylim((0,1000))

#     ax2 = ax1.twinx()
#     ax2.set_ylabel('Setup time (ms, dashed)', fontsize='xx-large')
#     ax2.set_ylim((0,1000))

#     x_min = 10
#     x_max = 20
#     xs = [2 ** exp for exp in range(x_min, x_max + 1)]

#     for name in names:
#         color = color_map[name]
#         filepath = folder + '/' + name

#         lower, mean, upper = folder_summary(filepath, x_min, x_max, color, 'experiment')
#         ax1.plot(xs, mean, color=color)
#         ax1.scatter(xs, mean, color=color)
#         ax1.fill_between(xs, lower, upper, alpha=0.1, color=color)

#         lower, mean, upper = folder_summary(filepath, x_min, x_max, color, 'setup')
#         ax2.plot(xs, mean, color=color, linestyle='dashed')
#         ax2.scatter(xs, mean, color=color, marker='D')
#         ax2.fill_between(xs, lower, upper, alpha=0.1, color=color)


    # font = {'family' : 'normal',
    #     'weight' : 'bold',
    #     'size'   : 22}

    # matplotlib.rc('font', **font)

    # for tick in ax1.xaxis.get_major_ticks():
    #     tick.label.set_fontsize(22)

    # for tick in ax1.yaxis.get_major_ticks():
    #     tick.label.set_fontsize(22)

    # for tick in ax2.yaxis.get_major_ticks():
    #     tick.label.set_fontsize(18)

    # plt.legend(names, fontsize='xx-large')
    # plt.grid()
    # plt.show()


if __name__ == '__main__':
    plt.style.use('mine.mplstyle')

    names = ['ChainedDivisionHashTable', 'HashTableBitmap', 'Bitmap']
    memory_folder = 'htb-memory-2'
    time_folder = 'htb'
    time_folders = [f'{time_folder}/{name}' for name in names]
    MIN_EXPONENT = 10
    MAX_EXPONENT = 22

    fig, ax1 = plt.subplots()
    ax1.set_xlabel('$n$')
    ax1.set_ylabel('Execution Time (ms, solid)')

    xs = [2 ** exp for exp in range(MIN_EXPONENT, MAX_EXPONENT + 1)]

    for name in names:
        folder = time_folder + '/' + name
        color = color_map[name]
        lower, mean, upper = folder_summary(folder, MIN_EXPONENT, MAX_EXPONENT, color_map[name])
        ax1.plot(xs, mean, color=color)
        ax1.scatter(xs, mean, color=color)
        ax1.fill_between(xs, lower, upper, color=color, alpha=0.1)
    # ax1.legend(names, fontsize='36', loc='lower center', bbox_to_anchor=(0, -0.6), mode='expand', frameon=False, ncol=1, borderaxespad=0)
    ax1.legend(['Hash Table', 'HTB', 'Bitmap'], fontsize='36')
    plt.grid()

    ax2 = ax1.twinx() 
    ax2.set_ylabel('Memory Usage (#bytes, dashed)')
    for name in names:
        if name == 'Bitmap':
            continue
        color = color_map[name]
        lists = getFileList(memory_folder + '/' + name)
        avgs = [statistics.mean(l) for l in lists]
        ax2.plot(xs, avgs, color=color, linestyle='dashed')
        ax2.scatter(xs, avgs, color=color, marker='x', s=200)


    plt.show()