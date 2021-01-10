# Reach plc. Apps Team Exercise

## Description

One of our magazines is looking for new sources of revenues and starts a few partnerships with beauty brands to sell their products within the app.
Users can buy any number of these products in any particular order. This is an example list of products:

``` 
ID            | Name                |  Price 	| Image
--------------------------------------------------------------
LIPSTICK      | Express Lipstick    |   22.00€	| https://....
EYELINER      | Daily Eyeliner      |   50.00€	| https://....
SHAMPOO       | RSVP Shampoo   		|   35.50€	| https://....
```

And we want to give the user the following discounts:

 * Buy two Express Lipstick's, get one free

 * Buy 3 or more Daily Eyeliner's and the price per unit should be 30.00€


Examples:

    Items: LIPSTICK, LIPSTICK, SHAMPOO
    Total: 107.50€

    Items: LIPSTICK, EYELINER, LIPSTICK
    Total: 72.00€

    Items: EYELINER, EYELINER, EYELINER, LIPSTICK, EYELINER
    Total: 170.00€

    Items: LIPSTICK, EYELINER, LIPSTICK, LIPSTICK, SHAMPOO, EYELINER, EYELINER
    Total: 169.50€

---

## Objective:

- Implement an app that lists the beauty products and allows the user to choose which ones to buy. Include a simple checkout view, displaying which items the user will buy, the total price and which discounts have been applied.
- You may get the list of products from here [https://apps-tests.s3-eu-west-1.amazonaws.com/android/products.json](https://apps-tests.s3-eu-west-1.amazonaws.com/android/products.json).
- No need to implement login/signup. No need to implement an actual payment system. No need to include any shipping details. It's enough to simulate a successfull payment and clear the checkout.
- Bonus points for making the app work offline, handling config changes or cool UI details.

## Guidelines

- Write code that is going to production. We'd like to see how you program in your day-to-day.
- We're looking for code that's easy to read, easy to maintain and easy to grow.
- Prefer less code written well, than writing code in a rush.
- It's ok to use commonly used Android libraries (image loading, networking, jetpack, dependency injection, RxJava), but try to keep dependencies to a minimum.
- There is not any minimum requirement for the Android API level.
- Create a `NOTES.md` file explaining your solution, implementation details and trade-offs. Writting good documentation is a rare but important skill.


## Submission

- You can zip the final project and send us by email. Or push to a public GitHub repository.
- If something is not clear, feel free to drop us an e-mail.
- We will give you 10 days to finish the exercise.
- It's OK if you need more time, it's also OK if you take shortcuts, as long as you document it well or communicate with us in advance.

===================================================================================================================


