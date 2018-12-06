Assignment 8
A How-to-invest-for-dummies application
Using MVC Design Pattern

Team:
Michael Lopez and Kshitij Mourya

Summary of design changes:
Release 1.3:

There were no major overall design changes in the layout of the design pattern. However there were many key changes within the model itself and the 
addition of a GUI in this release. 

Before, whenever the model was asked to purchase stock, it was required to ask the online API everytime and required a slowdown in processing time to 
keep the API from locking the API key access to the infomrtion. Now, the model will save the infomation obtained from the API for each stock in 
separate csv files and use these files to perform all previously defined features from then on. This allows for shorter run times after the initial
buying of a stock. With this save feature also came the framework to add a feature to save the account portfolios. Now before the user buys stock, 
the state of the account, its portfolios, and all retained stocks are saved in the users working directory in autosave fashion with no need for input 
from the user. This new feature goes hand in hand with another new feature to load a previous state of the account from a save state. 

The GUI is the exact same as the previous view layout now in a popup window. There was no change in the general layout of either the previous view, or 
the way it is seen in the GUI. The program will first ask which interface the user prefers and then instantiate the chosen one for the user.

Release 1.2:

There were no major changes in design. The biggest change was the moved of the output and input methods from the controller to the view; this was done 
in an attempt to have a more traditional MVC design. Other changes were made primarily in the model implementation and method signatures. To compensate
for the resitrictions from the API (5 calls per minute) we imposed Thread.sleep() after or before calls to the API to slow the process of buying stocks
and redcue the chance of getting an error from an overloaded API. The cost bases (cost vs. profit) printing was separated from the printing of the rest
of the account details to allow for smoother testing and user-end flexibility.

Introduction to the application:
The application created for the assignment is a preliminary version of a stock market simulation application.
It can be used to introduce a newbee to the stock market and how the stock market works.
It simulates the behaviour of the stock market in real time and without any actual money involved, it is a safe way for a person to learn stock trading.

Functionalities/Features of the application:
Since, it's a very basic version, it has limited capabilities as of now. 
The current features are:
1) Allows user to create portfolios and buy stocks in them.
2) Examine the portfolios.
3) Get the total cost and current values of the stocks/portfolios.
4) Apply and save specific dollar cost averageing investment strategies.
5) Save and load account, portfolio, stock information to and from file.

Our application takes input in text interface format. It is similar to the person calling a callcentre. 
The response a user gives is limited to the conditions mentioned. Generally that is typing in the appropriate number as required.
If user gives incorrect input, it asks user to input again.

Apart from that, we have incorporated the method to sell the stocks and remove portfolio which were not required but surely would be a part of real simulation.
Sell the stocks is not thouroughly tested and there is room for expansion and improvement.
Remove portfolio as of now deletes everything in that portfolio. No information is saved. This is to be changed according to the implementation required.

Design of the application:
The application is created in MVC design pattern. 

The user interacts with the application through text based interface. The user is made to follow the commands on screen.
Any other input throws exception.

The view calls the controller.

Controllers parses the input and gives out outputs for view to display.
go method in the controller communicates with the model.

Interface "UserAccount" contains all the public methods in the model.
Controller calls the methods from this interface. The methods are implemented in the class "Account.java".
The methods are addPortfolio, removePortfolio,buyStock,sellStock and viewAccount.
addPortfolio: adds a new portfolio in the user's account.
removePortfolio: removes everything associated with that portfolio. This method needs to be improved according to requirements.
buyStock: It buys the stock. It asks for name of stock,number of shares, the price type( currently hardcoded to "open") and the portfolio name.
sellStock: It sells the stock. This method is under development. Works but buggy. Not a requirement right now.
viewAccount. It shows the account details and whether the user made a profit or not.
 
Stock.java is a class which has several private methods which helps the above mentioned methods.
getTicker= gets the tickr symbol from the API when company name is provided.
getShares= returns the shares.
setShares= sets the shares.
getCost= returns the cost.
setCost= sets the cost.

APIData.java interacts with the alphavnatage API. All API calls are done here.
Two methods
searchCode(): It returns the tickr symbol from the company name. It uses the search endpoint of alphavantage API.
getPrices(): It gets the prices of the shares through the tickr code passed.


Restrictions of the Application:
Currently only 5 API calls can be made per minute or 500 API calls a day. If it exceeds them then exception occurs.
It could be solved by getting multiple apikeys  or getting a premium/paid API call functionality.

The application asks user to enter Date and time when application is run. 

If system time is taken then it would be difficult to test when the market is actually closed.
So it makes more sense to enter time manually.
Same goes for system date.
 


Please do check the class diagram as well.




