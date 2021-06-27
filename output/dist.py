from process import file_summary
import itertools
import matplotlib.pyplot as plt
import scipy.stats
import numpy as np
from sklearn.preprocessing import StandardScaler
import warnings

plt.style.use('mine.mplstyle')
warnings.filterwarnings("ignore")

def get_all_values(filepaths):
    setup_times = []
    experiment_times = []
    for filepath in filepaths:
        setup_time, experiment_time = file_summary(filepath)
        setup_times.append(setup_time)
        experiment_times.append(experiment_time)

    setup_times = list(itertools.chain.from_iterable(setup_times))
    experiment_times = list(itertools.chain.from_iterable(experiment_times))
    return setup_times, experiment_times


def get_ss_p(y, y_std, distribution, cum_observed_frequency, percentile_cutoffs, percentile_bins):
    # Set up distribution and get fitted distribution parameters
    try:
        dist = getattr(scipy.stats, distribution)
        param = dist.fit(y_std)
        # print(f'{distribution} - {param}')
    except:
        print(f'Something went wrong in {distribution}')
        return None

    p = scipy.stats.kstest(y_std, distribution, args=param)[1]

    # Get expected counts in percentile bins
    # cdf of fitted sistrinution across bins
    cdf_fitted = dist.cdf(percentile_cutoffs, *param)
    expected_frequency = []
    for bin in range(len(percentile_bins) - 1):
        expected_cdf_area = cdf_fitted[bin + 1] - cdf_fitted[bin]
        expected_frequency.append(expected_cdf_area)

    expected_frequency = np.array(expected_frequency) * len(y)
    cum_expected_frequency = np.cumsum(expected_frequency)
    ss = sum(((cum_expected_frequency - cum_observed_frequency) ** 2) / cum_observed_frequency)

    return ss, p


def plot_hist_line(y, dist_name, h):
    dist = getattr(scipy.stats, dist_name)
    param = dist.fit(y)
    
    # Get line for each distribution (and scale to match observed data)
    # x = np.linspace(0, 300, 100)
    x = np.arange(0, max(y))
    pdf_fitted = dist.pdf(x, *param[:-2], loc=param[-2], scale=param[-1])
    scale_pdf = np.trapz (h[0], h[1][:-1]) / np.trapz (pdf_fitted, x)
    pdf_fitted *= scale_pdf
    
    # Add the line to the plot
    plt.plot(pdf_fitted, label=dist_name)
    
    # Set the plot x axis to contain 99% of the data
    # This can be removed, but sometimes outlier data makes the plot less clear


def plot_hist(y, dist_names, number_of_bins = 10):
    bin_cutoffs = np.linspace(np.percentile(y,0), np.percentile(y,99),number_of_bins)
    h = plt.hist(y, bins = bin_cutoffs, color='0.75')

    for dist in dist_names:
        plot_hist_line(y, dist, h)

    plt.xlim(0, np.percentile(y,100))
    plt.ylim(((min(h[0])), max(h[0]) + 5))
    plt.legend()


def plot_qq_ss(y_std, distribution):
    data = y_std.copy()
    data.sort()
        # Set up distribution
    dist = getattr(scipy.stats, distribution)
    param = dist.fit(y_std)
    
    # Get random numbers from distribution
    norm = dist.rvs(*param[0:-2],loc=param[-2], scale=param[-1],size = len(y_std))
    norm.sort()
    
    # Create figure
    fig = plt.figure(figsize=(8,5)) 
    
    # qq plot
    ax1 = fig.add_subplot(121) # Grid of 2x2, this is suplot 1
    ax1.plot(norm,data,"o")
    min_value = np.floor(min(min(norm),min(data)))
    max_value = np.ceil(max(max(norm),max(data)))
    ax1.plot([min_value,max_value],[min_value,max_value],'r--')
    ax1.set_xlim(min_value,max_value)
    ax1.set_xlabel('Theoretical quantiles')
    ax1.set_ylabel('Observed quantiles')
    title = 'qq plot for ' + distribution +' distribution'
    ax1.set_title(title)
    
    # pp plot
    ax2 = fig.add_subplot(122)
    
    # Calculate cumulative distributions
    bins = np.percentile(norm,range(0,101))
    data_counts, bins = np.histogram(data,bins)
    norm_counts, bins = np.histogram(norm,bins)
    cum_data = np.cumsum(data_counts)
    cum_norm = np.cumsum(norm_counts)
    cum_data = cum_data / max(cum_data)
    cum_norm = cum_norm / max(cum_norm)
    
    # plot
    ax2.plot(cum_norm,cum_data,"o")
    min_value = np.floor(min(min(cum_norm),min(cum_data)))
    max_value = np.ceil(max(max(cum_norm),max(cum_data)))
    ax2.plot([min_value,max_value],[min_value,max_value],'r--')
    ax2.set_xlim(min_value,max_value)
    ax2.set_xlabel('Theoretical cumulative distribution')
    ax2.set_ylabel('Observed cumulative distribution')
    title = 'pp plot for ' + distribution +' distribution'
    ax2.set_title(title)
    
    # Display plot    
    plt.tight_layout(pad=4)


def main():
    filepathss = [[f'perfect-hash-table-normal/ChainedDivisionHashTable/t-{j}-{i}' for i in range(1)] for j in range(18, 20)]

    for filepaths in filepathss:
        print('-' * 50)
        setup, experiment = get_all_values(filepaths)

        y = np.array(experiment)
        sc = StandardScaler() 
        yy = y.reshape (-1,1)
        sc.fit(yy)
        x = np.arange(len(y))
        y_std = sc.transform(yy)
        y_std = y_std.flatten()
        size = len(y)
        del yy

        dist_names = ['invgamma', 'norm']

        # dist_names = ['loggamma', 'invgamma', 'norm', 'laplace', 'pareto', 'johnsonsb', 'expon', 'weibull_min']
        # dist_names = ['invgamma', 'johnsonsb', 'norm', 'laplace']
        # dist_names = [name for name in dir(scipy.stats) 
            # if isinstance(getattr(scipy.stats, name), scipy.stats.rv_continuous)
            # and name not in ['levy_stable']]

        percentile_bins = np.linspace(0, 100, 10)
        percentile_cutoffs = np.percentile(y_std, percentile_bins)
        observed_frequency, bins = (np.histogram(y_std, bins=percentile_cutoffs))
        cum_observed_frequency = np.cumsum(observed_frequency)

        chi_square_statistics = []
        p_values = []

        for distribution in dist_names:
            result = get_ss_p(y, y_std, distribution, cum_observed_frequency, percentile_cutoffs, percentile_bins)
            if result is not None:
                chi_square_statistics.append(result[0])
                p_values.append(result[1])


        ordered = sorted(zip(dist_names, chi_square_statistics, p_values), key=lambda t: t[1])
        for dist, chi2, p in ordered:
            print(f'{dist} - {chi2} - {p}')

        plot_hist(y, dist_names)
        # for dist in dist_names:
        #     plot_qq_ss(y_std, dist)

        plt.ylabel('Amount of (expected) Observations')
        plt.xlabel('Execution Time (ms)')
        plt.grid(True)
        plt.show()
        # plt.show()


if __name__ == '__main__':
    main()

    #bitmap -- inverse gamma
    #redblacktree -- invgamma, johnsonsb
    #binarytrie -- invgamma, johnsonsb
    #vebtree -- invgamma, johnsonsb
    #hashtable -- invgamma
