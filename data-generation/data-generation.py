from scipy.stats import uniform, norm
import math, random, sys
import numpy as np
import matplotlib.pyplot as plt
import itertools


def generate_uniform_data(amount, lower_bound, upper_bound):
    read = uniform.rvs(size=amount)
    return np.vectorize(lambda x: lower_bound + math.floor(x * (upper_bound - lower_bound)))(read)


def generate_gaussian_data(amount, mean, sd, lower_bound, upper_bound):
    read = norm(mean, sd).rvs(size=amount)
    return np.vectorize(lambda x: math.floor(max(min(x, upper_bound), lower_bound)))(read)


def generate_uniform_among_gaussian(amount, means, sds, lower_bound, upper_bound):
    if len(means) != len(sds):
        raise ValueError('means and sds should have the same length')

    distribution_amount = int(amount / len(means))

    distributions = list(zip(means, sds))
    result = []

    for dist in distributions:
        result.extend(norm(*dist).rvs(size=distribution_amount))

    result = list(map(int, result))
    random.shuffle(result)

    return result


SETUP = 'SETUP'
EXPERIMENT = 'EXPERIMENT'
INSERT = 0
IS_MEMBER = 1
DELETE = 2


def write_to_file(filename, setup_data, experiment_operations, experiment_data):
    if len(experiment_data) != len(experiment_operations):
        raise ValueError('The number of numbers in the experiment should be the same as the number of operations')

    if any(op not in [INSERT, IS_MEMBER, DELETE] for op in experiment_operations):
        raise ValueError('Only INSERT, IS_MEMBER and DELETE operations are allowed.')

    with open(filename, 'w+') as file:
        file.write(f'{SETUP} {len(setup_data)}\n')
        file.writelines(map(lambda s: str(s) + '\n', setup_data))
        file.write(f'{EXPERIMENT} {len(experiment_data)}\n')
        
        for (operation, number) in zip(experiment_operations, experiment_data):
            file.write(f'{operation} {number}\n')
    

INSERT = 0
IS_MEMBER = 1
DELETE = 2
U_min = -2**31
U_max = 2**31 - 1

# if __name__ == '__main__':
#     insertions = np.array(generate_uniform_among_gaussian(2**14, [2**20, 2**30], [2**18, 2**18], U_min, U_max))
#     false_queries = np.array(generate_uniform_among_gaussian(2**14, [2**20, 2**30], [2**18, 2**18], U_min, U_max))
#     false_queries = [x for x in false_queries if x not in insertions]

#     with open('test-multiple-normal-insert.data', 'w+') as file:
#         for number in insertions:
#             file.write(str(number))
#             file.write('\n')

#     with open('test-multiple-normal-false-is-member.data', 'w+') as file:
#         for number in false_queries:
#             file.write(str(number))
#             file.write('\n')

if __name__ == '__main__':
    FILES_PER_EXPONENT = 1
    MIN_EXPONENT = 10
    MAX_EXPONENT = 20
    MEAN = 2**28
    SD = 2**25


    QUERY_AMOUNT = int(2**20 / 3)

    for exp in range(MIN_EXPONENT, MAX_EXPONENT + 1):
        print(f'Exponent: {exp : <5}')
        for file_id in range(0, FILES_PER_EXPONENT):
            print(f'  Iteration: {file_id : <2}')
            setup_amount = 2**exp
            setup_numbers = generate_gaussian_data(setup_amount, MEAN, SD, U_min, U_max)

            instructions = []
            delete_numbers = random.sample(list(setup_numbers), min(setup_amount, QUERY_AMOUNT))
            if len(delete_numbers) < QUERY_AMOUNT:
                delete_numbers.extend(generate_gaussian_data(QUERY_AMOUNT - len(delete_numbers), MEAN, SD, U_min, U_max))

            delete_instructions = zip(delete_numbers, itertools.repeat(DELETE))
            is_member_instructions = zip(generate_gaussian_data(QUERY_AMOUNT, MEAN, SD, U_min, U_max), itertools.repeat(IS_MEMBER))
            insert_instructions = zip(generate_gaussian_data(QUERY_AMOUNT, MEAN, SD, U_min, U_max), itertools.repeat(INSERT))

            instructions.extend(insert_instructions)
            instructions.extend(is_member_instructions)
            instructions.extend(delete_instructions)

            random.shuffle(instructions)

            instruction_numbers = [num for num, _ in instructions]
            instructions_ops = [op for _, op in instructions]
            write_to_file(f'dynamic-normal/n-2^{exp}-{file_id}.data', setup_numbers, instructions_ops, instruction_numbers)


    
# if __name__ == '__main__':
#     FILES_PER_EXPONENT = 10
#     MIN_EXPONENT = 10
#     MAX_EXPONENT = 20

#     EXPERIMENT_AMOUNT = 2**20

#     MEANS = [2**28, -2**28]
#     SDS = [2**25] * 2
#     U_min = -2**31
#     U_max = 2**31 - 1


#     for exp in range(MIN_EXPONENT, MAX_EXPONENT + 1):
#         print(f'Exponent: {exp : <5}')
#         for iter in range(FILES_PER_EXPONENT):
#             print(f'  Iteration: {iter : <2}')
#             SETUP_AMOUNT = 2 ** exp
#             setup_numbers = generate_uniform_among_gaussian(0, MEANS, SDS, U_min, U_max)
#             instruction_numbers = generate_uniform_among_gaussian(2 ** exp, MEANS, SDS, U_min, U_max)
#             instructions_ops = [DELETE] * len(instruction_numbers)
#             write_to_file(f'gaussian-delete-only/n-2^{exp}-{iter}.data', instruction_numbers, instructions_ops, instruction_numbers)
