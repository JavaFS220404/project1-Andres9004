# Employee Reimbursment System (ERS)

## Executive Summary
The Expense Reimbursement System (ERS) will manage the process of reimbursing employees for expenses incurred while on company time. All employees in the company can login and submit requests for reimbursement and view their past tickets and pending requests. When submitting a reimbursment request Employees must be able to select the type of reimbursement as: LODGING, TRAVEL, FOOD, or OTHER. Finance managers can log in and view all reimbursement requests and past history for all employees in the company. Finance managers are authorized to approve and deny requests for expense reimbursement.

**State-chart Diagram (Reimbursement Statuses)** 

![](./imgs/state-chart.jpg)


**Logical Model**

![](./imgs/logical.jpg)

**Physical Model**

![](./imgs/physical.jpg)

**Use Case Diagram**

![](./imgs/use-case.jpg)

**Activity Diagram**

![](./imgs/activity.jpg)

## Technical Requirements

The back-end system shall use JDBC to connect to a Postgres database. The middle tier shall use Sevlet technology for dynamic Web application development. The front-end view shall use HTML/CSS/JavaScript to make an application that can call server-side components in a generally RESTful manner. The middle tier shall follow proper layered architecture, and have reasonable JUnit test coverage of the service layer. Webpages shall be styled to be functional and readable. 

**Stretch Goals (if you'd like to use this technology):**

* Users can upload a document or image of their receipt when submitting reimbursements which can stored in the database and reviewed by a financial manager.
* Employees are organized into departments and finance managers can only approve requests for the department(s) they oversee. 
* Application shall be hosted remotely on an EC2.
* Static files (webpages) shall be hosted on an S3 bucket.


## Misc Notes

* This README should prove helpful when getting the big picture of everything you need to do... However, what should be most helpful to you is actually looking through the skeleton and seeing the required functionality. COE was very generous to already create the required method signatures for you and leave notes on how the various classes should work.

* For the simple reason of time management you will need to be working on the project before we have covered every single topic you will need to complete the project. The included ![milestones.md](./milestones.md) is a good guide for where you should be through each week of training with the project requirements to stay on pace. 

* Remember the logical progression of our java applications. We tend to go from the main method, to the controller layer, to the service layer, to the DAO (aka repository) layer. There are exceptions to this rule of thumb, but in general it's a good rule to follow, and the skeleton is structured accordingly. 


* Your trainer can only teach you so much -- This project will require you to do a bit of self study. (For instance, learning about how enums work. They aren't that scary, really.) Your BEST resource when self studying besides google is each other. You're in teams for a reason! This is a great opportunity to figure out how to articulate your problems to others and help others with their own problems. Web dev is not an introverted role...

* When creating your applications, the diagrams above are simply suggestions for best practice... There are easier and harder ways to implement the same thing. For instance: 
    * The reimbursements table in your database only **needs** the 5 required fields found in the AbstractReimbursement Class. Similar story with the users table. Further, the reimbursement status is a separate table in the logical and physical models, but you may opt to simply make status a field in the reimbursement table to skip the addition of the extra table (though it's good practice).
    * The Use Case and Activity diagrams are **not** all of the required functionalities you need, but are good to look at to make sure your application is flowing properly. 


* Let me reiterate - HELP EACH OTHER! Don't spend 3 hours on the same problem when you could talk it out with your peers after ~1 hour of bashing your head against stackoverflow.com. But of course, do you own work and make sure to actually learn or you'll have a bad time during P2. 

## Frequently Asked Questions

1. When is the project due? 

    >A: The project will be due the Friday of Week 5. At that point you will be expected to present the state of your application in a project showcase. 

2. Can I have an extension? 
    >A: No. While you are encouraged to continue to work on your projects past the date of the project presentation for your own learning benefit, due to the pace of training extensions cannot be accommodated. If for some reason you cannot be present at the time of project presentations you will need to schedule a time BEFORE the due date to present your project. 

3. Is there a code freeze? 
    >A: It is recommended that you institute your own code freeze at least a day before the project presentations. However, this is a recommendation only; it will not be enforced. NOTE: The code that will be evaluated by your trainer will be the code you last pushed to your repository BEFORE the time set for project presentations. Code submitted while presentations are on-going or after will not be evaluated. 

4. What happens if I break (or accidently delete/destroy my computer with) my project that was mostly working right before the due date? 
    >A: As you should have been regularly pushing code to your repository you should be able to roll back to previously working version. If you have not regularly pushed your code and do not have a working commit to return to you will need to present the state of your application in its current form. If your computer has suffered a major catastrophe immedately before presentations and you do not have a device to run the project your trainer can pull your code and run the application while you present it.  

5. Who will be evaluating the project? 
    >A: Your trainer will be the one providing the full evaluation of your projects. However, the QC team will also be present at presentations to ask questions about your project and consult with your trainer. 

6. Are we allowed to collaborate with others on our projects? 
    >A: Collaboration is encouraged. Hopefully you will work together to solve the problems presented in this project. However, you should still be ultimately designing the project yourself. Straight copy/pasting of another person's code is considered plagiarism. NOTE: Code provided in demos by your trainer is not subject to plagiarism concerns. Feel free to copy/paste and edit such code as necessary to suit your projects. 

7. What is a passing score? 
    >A: The project is not graded with a traditional scoring system. It is graded on a rubric based on the functionality of the application and your presentation of that functionality. A project that's functionality meets the minimum viable product description above that is presented clearly during the project showcase will pass. 

8. What are stretch goals?
  >A: Stretch goals are extensions to the project that if completed will grant bonus points. However, stretch goals will only be considered if all the required functionality (i.e. requirements that are not stretch goals) are completed. 

8. If I fail the project will I be released from training?  
    >A: The project is a major component of the determination process for continued participation in training. That said, while your overall evaluation score in training is heavily weighted towards projects it is not the only factor in that evaluation. Quizzes, 1 on 1s, QC and any additional evaluations (Coding challenges/Scrum Team participation etc.) will also factor into any release decision. 

9. Are there really no extensions? 
    >A: There are not.
