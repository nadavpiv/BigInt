# BigInt Project

## Project Overview

This project involves implementing a `BigInt` class in Java to handle theoretically "unlimited" integers. The class provides basic arithmetic operations (addition, subtraction, multiplication, division) and supports comparison and string representation.

## Class Implementation

### `BigInt` Class
- **Constructor**: Takes a string and creates a `BigInt`. The number is stored using an `ArrayList` where each element represents a digit. Handles positive and negative signs. Throws `IllegalArgumentException` for invalid input.
- **`plus(BigInt other)`**: Returns a new `BigInt` that is the sum of the current number and the `other` parameter. Handles addition of positive and negative numbers.
- **`minus(BigInt other)`**: Returns a new `BigInt` that is the difference between the current number and the `other` parameter. Manages subtraction with correct sign handling.
- **`multiply(BigInt other)`**: Returns a new `BigInt` that is the product of the current number and the `other` parameter. Implements multiplication by iterating over digits and combining results.
- **`divide(BigInt other)`**: Returns a new `BigInt` that is the quotient of the current number divided by the `other` parameter (integer division). Throws `ArithmeticException` for division by zero.
- **`toString()`**: Returns a string representation of the `BigInt`.
- **`equals(Object other)`**: Overrides `equals` method from `Object` to compare two `BigInt` instances.
- **`compareTo(BigInt other)`**: Implements the `Comparable` interface. Returns a negative, zero, or positive number if the current `BigInt` is less than, equal to, or greater than the `other` parameter.

### Main Program

- A separate class contains the main program that uses the `BigInt` class.
- The program prompts the user to input two strings representing `BigInt` numbers.
- It then performs the defined arithmetic operations and displays the results.
- Handles exceptions by:
  - Prompting for a valid number if an invalid string is provided.
  - Displaying an appropriate message if division by zero is attempted.
