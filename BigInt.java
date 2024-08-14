import java.util.ArrayList;
import static java.lang.Character.isDigit;
import java.lang.Math;
import java.util.Collections;


public class BigInt implements Comparable<BigInt>{
    private ArrayList<String> bigNumber;
    private final int SMALL_THAN = -1;
    private final int BIGGER_THAN = 1;

    // The constructor get string and put him in array list that represents the big number.
    public BigInt(String number) throws IllegalArgumentException{
        bigNumber = new ArrayList<>();
        for (int i = 0; i < number.length(); i++){
            // If we got char that isn't digit and this is not the first char this is illegal number.
            if((!isDigit(number.charAt(i)) && i > 0) || (isDigit(number.charAt(i)) && i == 0) ||
                    (i == 0 && number.charAt(i) != '-' && number.charAt(i) != '+'))
                    throw new IllegalArgumentException("Illegal number");
            bigNumber.add(String.valueOf(number.charAt(i)));
        }
    }

    // Copy constructor.
    public BigInt(BigInt other){
        this.bigNumber = new ArrayList<>();
        bigNumber.addAll(other.bigNumber);
    }

    // This method print the big number.
    public String toString(){
        String tempStr = "";
        for (String s : bigNumber)
            tempStr += s;
        return tempStr;
    }

    // This method make sum operation between 2 big numbers and return the result.
    public BigInt plus (BigInt other) throws IllegalArgumentException {

        // create copy of the numbers, because we don't want to change the real numbers.
        BigInt copyThis = new BigInt(this);
        BigInt copyOther = new BigInt(other);

        // If one of the numbers negative and one positive minus function will do the calculation.
        if((bigNumber.get(0).equals("-") && other.bigNumber.get(0).equals("+"))){
            copyThis.bigNumber.set(0,"+"); // negative + positive = positive - (-1 * negative).
            return copyOther.minus(copyThis);
        }
        else if(bigNumber.get(0).equals("+") && other.bigNumber.get(0).equals("-")){
            copyOther.bigNumber.set(0,"+"); // positive + negative = positive - (-1 * negative).
            return copyThis.minus(copyOther);
        }

        // Add lead zeroes and reverse the numbers for simple calculation.
        addLeadZeroes(copyThis,copyOther);
        copyThis.reverseBigNumber();
        copyOther.reverseBigNumber();

        // Make 2 strings for the big numbers, original and parameter.
        String original = String.join("", copyThis.bigNumber);
        String parameter = String.join("", copyOther.bigNumber);
        String newNumber = "";
        int rem = 0;
        int max = Math.max(original.length(),parameter.length());

        for(int i = 0; i < max-1; i++){
            int tempNum = Integer.parseInt(String.valueOf(original.charAt(i))) +
                        Integer.parseInt(String.valueOf(parameter.charAt(i))) + rem;
            if (tempNum >= 10){ // If we have reminder we need to change rem variable.
                tempNum = tempNum % 10;
                rem = 1;
            }
            else
                rem = 0; // If we don't have reminder.

            newNumber = tempNum + newNumber; // Add the sum to the string.
        }

        if (rem == 1) // If the last sum was bigger than 1 we have to add another 1.
            newNumber = "1" + newNumber;

        //plus between 2 negative numbers.
        if (original.charAt(original.length()-1) == '-' && parameter.charAt(parameter.length()-1) == '-')
            newNumber = "-" + newNumber;
        else
            newNumber = "+" + newNumber;

        // Built and return the new number.
        BigInt res = new BigInt(newNumber);
        return res.organiseNumber();
    }

    // This method make subtract operation between 2 big numbers and return the result.
    public BigInt minus(BigInt other){

        // create copy of the numbers, because we don't want to change the real numbers.
        BigInt copyThis = new BigInt(this);
        BigInt copyOther = new BigInt(other);
        addLeadZeroes(copyThis,copyOther);


        /* If we have sub between 2 negative numbers, we can do subtract between 2 positive
        and match the sign at the end */
        boolean doubleNegative = false;

        /* If the numbers have different sign we can change the negative to be positive and use
        the plus method and at the end to match the correct sign to the number. */
        if(bigNumber.get(0).equals("+") && other.bigNumber.get(0).equals("-")){
            copyOther.bigNumber.set(0,"+"); // positive - negative = positive + (-1 * negative).
            return copyThis.plus(copyOther);
        }

        else if(bigNumber.get(0).equals("-") && other.bigNumber.get(0).equals("+")){
            copyOther.bigNumber.set(0,"-"); // negative - positive = negative + (-1 * positive)
            return copyThis.plus(copyOther);
        }

        // negative - negative = negative + (-1 * negative) = negative + positive
        else if(copyThis.bigNumber.get(0).equals("-") && copyOther.bigNumber.get(0).equals("-")){
            copyThis.bigNumber.set(0,"+");
            copyOther.bigNumber.set(0,"+");
            doubleNegative = true;
        }
        String max; // Keep the biggest number.
        String min; // Keep the smallest number.
        String newNumber = "";

        // Check which number is bigger for the sub operation.
        if(copyThis.compareTo(copyOther) == BIGGER_THAN){
            copyThis.reverseBigNumber();
            copyOther.reverseBigNumber();
            max = String.join("", copyThis.bigNumber);
            min = String.join("", copyOther.bigNumber);
        }

        else if(copyThis.compareTo(copyOther) == SMALL_THAN){
            copyThis.reverseBigNumber();
            copyOther.reverseBigNumber();
            max = String.join("", copyOther.bigNumber);
            min = String.join("", copyThis.bigNumber);
        }

        else // The numbers are equal so the result of the sub is zero.
            return new BigInt("+0");


        int borrow = 0;
        for (int i = 0; i < max.length()-1; i++){
            int tempNum; // Keep subtract of the 2 numbers in vertical subtract.
            tempNum = Integer.parseInt(String.valueOf(max.charAt(i))) - borrow -
                        Integer.parseInt(String.valueOf(min.charAt(i)));
            if(tempNum < 0){ // If the number < 0 we need to add 10 like vertical subtract.
                tempNum += 10;
                borrow = 1; // borrow need to be 1,so we will remember to subtract 1 from the next result.
            }
            else
                borrow = 0; // If subtract was > 0, the reminder will be 0.
            newNumber = tempNum + newNumber;
        }

        // Reverse the number again to check which bigger.
        copyThis.reverseBigNumber();
        copyOther.reverseBigNumber();

        // check if we have 2 negative or 2 positive, for the other option we have solutions already.
        if (!doubleNegative) { // double positive.
            if (copyThis.compareTo(copyOther) == BIGGER_THAN)
                newNumber = "+" + newNumber;
            else
                newNumber = "-" + newNumber;
        }
        else{ // double negative.
            if (copyThis.compareTo(copyOther) == BIGGER_THAN)
                newNumber = "-" + newNumber;
            else
                newNumber = "+" + newNumber;
        }

        // Build and return the big number.
        BigInt res = new BigInt(newNumber);
        return res.organiseNumber();
    }

    // This method make multiply operation between 2 big numbers and return the result.
    public BigInt multiply(BigInt other){

        // create copy of the numbers, because we don't want to change the real numbers.
        BigInt copyThis = new BigInt(this);
        BigInt copyOther = new BigInt(other);

        BigInt res = new BigInt("+0");
        // Reverse the numbers for simple calculations.
        copyThis.reverseBigNumber();
        copyOther.reverseBigNumber();

        // Make the big numbers strings, for simple calculations
        String original = String.join("", copyThis.bigNumber);
        String parameter= String.join("", copyOther.bigNumber);
        int multi = 0; // Keep the number to multiply every iteration, 1,10,100,1000.....
        int rem = 0;

        // In this loop we use the vertical multiplication method.
        for (int i = 0; i < original.length() - 1; i++){
            String tempBigNumber = "";
            for (int j = 0; j < parameter.length()-1; j++){
                int temp = Integer.parseInt(String.valueOf(original.charAt(i)))
                        * Integer.parseInt(String.valueOf(parameter.charAt(j))) + rem;
                if (temp > 10 && j < parameter.length()-2){
                    rem = temp / 10;
                    temp = temp % 10;
                }
                else
                    rem = 0;
                tempBigNumber = temp + tempBigNumber;
            }

            // Add multi zeroes to the end of the number like vertical multiply.
            tempBigNumber = tempBigNumber + String.join("", Collections.nCopies(multi, "0"));
            tempBigNumber = "+" + tempBigNumber;
            multi++;
            res = res.plus(new BigInt(tempBigNumber));
        }
        if ((bigNumber.get(0).equals("-") &&
                other.bigNumber.get(0).equals("+")) ||
                bigNumber.get(0).equals("+") &&
                        other.bigNumber.get(0).equals("-"))
            res.bigNumber.set(0,"-");

        return res.organiseNumber();
    }


    // This method make divide operation between 2 big numbers and return the result.
    public BigInt Divide(BigInt other) throws ArithmeticException{

        // Check that other is different from zero.
        if (other.compareTo(new BigInt("+0")) == 0
                || other.compareTo(new BigInt("-0")) == 0)
            throw new ArithmeticException("Can't divide by 0");

        boolean thisNegative = false;
        boolean otherNegative = false;

        // If we have negative number we change him to positive and save this for the result.
        if (this.bigNumber.get(0).equals("-")) {
            thisNegative = true;
            this.bigNumber.set(0, "+");
        }

        if (other.bigNumber.get(0).equals("-")) {
            otherNegative = true;
            other.bigNumber.set(0, "+");
        }

        BigInt reminder = new BigInt(this);
        BigInt res = new BigInt("+0");

        while (reminder.compareTo(other) == 1 || reminder.compareTo(other) == 0){
            BigInt tempRes = new BigInt("+1");
            BigInt copyOther = new BigInt(other);
            BigInt checkNumber = new BigInt(copyOther.plus(copyOther));

            // While check number is smaller than the reminder we multiply other number.
            // We want to check how many times other enter to the reminder.
            // We multiply the res because we multiply other number too.
            while (checkNumber.compareTo(reminder) == -1){
                copyOther = checkNumber;
                tempRes = tempRes.plus(tempRes);
                checkNumber = copyOther.plus(copyOther);
            }

            // After every iteration we increase the result and decrease the reminder for next iteration.
            res = res.plus(tempRes);
            reminder = reminder.minus(copyOther);
        }

        // Check what is the sign of the number, positive or negative.
        if (!(thisNegative && otherNegative) && (thisNegative == true || otherNegative == true))
            res.bigNumber.set(0,"-");
        return res.organiseNumber();
    }

    // This method check if the big numbers are equal.
    public boolean equals(Object other){
        if (!(other instanceof BigInt))
            return false;
        // If they are equals we will return true.
        return this.compareTo((BigInt) other) == 0;
    }

    // This method check which big number is bigger and return the result.
    @Override public int compareTo(BigInt other) {
        String original = String.join("", bigNumber);
        String parameter = String.join("", other.bigNumber);

        if(original.charAt(0) == '+' && parameter.charAt(0) == '-')
            return 1; // original positive and parameter negative.

        else if (original.charAt(0) == '-' && parameter.charAt(0) == '+')
            return -1; // original is negative and parameter is positive.

        else if(original.charAt(0) == '+' && parameter.charAt(0) == '+'){
            if(original.length() > parameter.length())
                return 1;

            else if (original.length() < parameter.length())
                return -1;

            else{ // The length of the numbers is equal.
                for (int i = 1; i < original.length(); i++){
                    if (original.charAt(i) > parameter.charAt(i)) // original > parameter.
                        return 1;

                    else if (original.charAt(i) < parameter.charAt(i)) // original < parameter.
                        return -1;
                }
                return 0; // The numbers are equal.
            }
        }

        else{ // the 2 numbers are negative.
            if(original.length() > parameter.length()) // original < parameter.
                return -1;

            else if (original.length() < parameter.length()) // original > parameter.
                return 1;

            else{ // the length of the numbers is equal.
                for (int i = 1; i < original.length(); i++){
                    if (original.charAt(i) > parameter.charAt(i)) // original < parameter.
                        return -1;

                    else if (original.charAt(i) < parameter.charAt(i)) // original > parameter.
                        return 1;
                }
                return 0; // The numbers are equal.
            }
        }
    }

    // This method reverse the array of the big number.
    public void reverseBigNumber(){
        int i = 0;
        int j = this.bigNumber.size()-1;
        while (i < j){
            String temp = this.bigNumber.get(i);
            this.bigNumber.set(i,bigNumber.get(j));
            this.bigNumber.set(j,temp);
            i++;
            j--;
        }
    }

    // this method change -0 to +0 and delete leading zeroes.
    public BigInt organiseNumber(){
        if (bigNumber.size() == 2 && bigNumber.get(1).equals("0"))
            bigNumber.set(0,"+");
        if(bigNumber.get(1).equals("0")){
            while(bigNumber.size() > 2 && bigNumber.get(1).equals("0"))
                bigNumber.remove(1);
        }
        return this;
    }

    /* This method add leading zeroes for the smaller number
    so the length of the numbers will be equal.*/
    public void addLeadZeroes(BigInt a, BigInt b){
        while (a.bigNumber.size() != b.bigNumber.size()){
            if(a.bigNumber.size() > b.bigNumber.size())
                b.bigNumber.add(1,"0");
            else
                a.bigNumber.add(1,"0");
        }
    }
}
