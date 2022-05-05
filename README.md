![Test Status](../../workflows/test/badge.svg)

# Gilded Rose Refactoring Kata

## Gilded Rose Requirements Specification

Hi and welcome to team Gilded Rose. As you know, we are a small inn with a prime location in a
prominent city ran by a friendly innkeeper named Allison. We also buy and sell only the finest goods.
Unfortunately, our goods are constantly degrading in quality as they approach their sell by date. We
have a system in place that updates our inventory for us. It was developed by a no-nonsense type named
Leeroy, who has moved on to new adventures. Your task is to add the new feature to our system so that
we can begin selling a new category of items. First an introduction to our system:

  - All items have a SellIn value which denotes the number of days we have to sell the item
  - All items have a Quality value which denotes how valuable the item is
  - At the end of each day our system lowers both values for every item

Pretty simple, right? Well this is where it gets interesting:

  - Once the sell by date has passed, Quality degrades twice as fast
  - The Quality of an item is never negative
  - "Aged Brie" actually increases in Quality the older it gets
  - The Quality of an item is never more than 50
  - "Sulfuras", being a legendary item, never has to be sold or decreases in Quality
  - "Backstage passes", like aged brie, increases in Quality as its SellIn value approaches;
  Quality increases by 2 when there are 10 days or less and by 3 when there are 5 days or less but
  Quality drops to 0 after the concert

We have recently signed a supplier of conjured items. This requires an update to our system:

  - "Conjured" items degrade in Quality twice as fast as normal items

Feel free to make any changes to the UpdateQuality method and add any new code as long as everything
still works correctly. However, do not alter the Item class or Items property as those belong to the
goblin in the corner who will insta-rage and one-shot you as he doesn't believe in shared code
ownership (you can make the UpdateQuality method and Items property static if you like, we'll cover
for you).

Just for clarification, an item can never have its Quality increase above 50, however "Sulfuras" is a
legendary item and as such its Quality is 80 and it never alters.

## What now?

The simplest way is to just clone the code and start hacking away improving the design.
Remember though, before refactoring, you have to have tests.

My suggestion is to either go ahead and write unit tests with junit5, or have a look at approval testing. 

## Text-Based Approval Testing

This is a testing approach which is very useful when refactoring legacy code. 
Before you change the code, you run it, and gather the output of the code as a plain text file. 
You review the text, and if it correctly describes the behaviour as you understand it, you can "approve" it, and save it as a "Golden Master". 
Then after you change the code, you run it again, and compare the new output against the Golden Master. 
Any differences and the test fails.

It's basically the same idea as "assertEquals(expected, actual)" in a unit test, except the text you are comparing is typically much longer, and the "expected" value is saved from actual output, rather than being defined in advance.

Typically, a piece of legacy code may not produce suitable textual output from the start, so you may need to modify it before you can write your first text-based approval test. 
That could involve inserting log statements into the code, or just writing a "main" method that executes the code and prints out what the result is afterwards. 
It's this latter approach we are using here to test GildedRose.

### Example

```java
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

class ApprovalTests {

    @Test
    void demo() {
        var input = "lars";

        CapitalisedString actual = new CapitalisedString(input);

        Approvals.verify(actual.toString());
    }

    static class CapitalisedString {

        private final String input;

        CapitalisedString(String input) {
            this.input = input.toUpperCase();
        }

        @Override
        public String toString() {
            return input;
        }
    }
}
```

When running this test for the first time, two files are created and the test fails.
As the name suggests, ApprovalTests.demo.received.txt contains the output of what was received in Approvals.verify's argument and 
ApprovalTests.demo.approved.txt is the output that is approved to be correct.
Thus, now after the first run, copy the content of the received file to the approved file and save.
When you run the test again now, it will pass.
The approved file has to be committed to the repository then.
 
It is also possible to run multiple inputs

```java
import org.approvaltests.combinations.CombinationApprovals;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class ApprovalTests {

    @Test
    void demo() throws Exception {
        String[] names = {"lars", "oskar"};
        Integer[] howOften = {2, 5};
        CombinationApprovals.verifyAllCombinations(this::methodThatReturnsString, names, howOften);
    }

    private String methodThatReturnsString(String input, int repeat) {
        return new CapitalisedRepeatedString(input, repeat).toString();
    }
    
    // CapitalisedString class like above.
}
```

In case of multiple parameters for the method reference, provide multiple arrays in the verifyAllCombinations call.
Also notice that these are no array of primitives, use Integer for int and such.
