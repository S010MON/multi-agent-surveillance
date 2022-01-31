## Workflow

1. Select card or issue from Projects
2. Make a new branch
3. Write tests
4. Make a Pull Request
5. Write Code!
6. Code review
7. Merge

## Select card or issue from Projects
All tasks should be tracked through the GitHub Projects system.  There will be a new Project made for each phase, so that things don't get too cluttered.  When issues are found they should be raised in the `issues` tab, or if a new feature is being made, this can be done in the projects directly (and then converted to an issue).  This system helps with two things:

a)  **Automated progress updates**. When you merge a pull request, the linked issues gets closed and the card is moved into the next column automatically.  We can track progress easily and not worry about maintaining a kanban board.

b) **Bugs**. When you find a bug, you might not be the one who knows how to solve it, so raising an issue will log the bug and allow someone else to take on the responsibility for a patch.

Projects can be found under the `projects` tab on the top bar
![image](https://github.com/S010MON/project-2-2/blob/master/sshots/projects.png)

Issues should be added with as much detail as possible in the `issues` tab, use the label and projects on the right hand side to link them to the board and add more info.
![image](https://github.com/S010MON/project-2-2/blob/master/sshots/completed_issue.png)

## Make a new branch
Everyone **must** develop each feature in a new branch and close that branch once the merge is complete.  Branches should be named after the *feature* in the card that it is for seperated by hypens like this: `name-of-card`.  Personal names, generics, and lazy naming aren't accepatble `leons_branch-13` is not handy for anyone to understand what the branch is for.

Why are branches deleted?  Once a branch is merged, that feature or bug is closed and the branch is considered out of date.  Continuing to develop in the branch will create merge conflicts and will make our lives much harder in the long run.  Keep branches open for short periods and keep them task based, you should be able to close one in a day or two of opening it, otherwise the tasks might be too big and should be split into seperate cards.

## Write tests
We'll employ Test Driven Development as much as possible, this is becuase someone will eventually break your code later on and (especially with the more numerical tasks) this can be impossible to spot early.  The CI/CD will automatically run your tests when you make a PR and writing your tests first forces you to think about what edge cases could occur.  Write the bare minimum code required to make your test fail like this:
```java
public int myAdder(int a, int b)
{
  return 0;  // Minimal implementation to make class/method compile
}
```
and add all the tests in a logical manner covering edge cases and invariance.

![image](https://github.com/S010MON/project-2-2/blob/master/sshots/labels.png)

## Make a Pull Request
Push your tests to GitHub and watch the code fail!  At this point you should make a Pull Request and link your issue, link your project and add your name.  This will update the card on the project and inform everyone that you have taken this issue to work on.

![image](https://github.com/S010MON/project-2-2/blob/master/sshots/pull_request.png)

## Write Code
When writing code, keep your commits often and leave useful messages.  If you mess up, you can only go back to your last commit, so if none of your tests were passing, and after hours of work they all are, but you haven't commited between, any accidental change will set you back to square one.  Leaving frequent commits will allow us to roll back to the last test that passed. A handy guide can be found ![here](https://www.freecodecamp.org/news/writing-good-commit-messages-a-practical-guide/)

## Code review
To make CI/CD work there is one rule: the master branch always runs! To ensure this, as well as passing the automated testing, every piece of code must be reviewd by another member of the group. This is to check for a few things which you **must** do if reviewing someone else's code

a) **Running**.  It should go without saying ... checkout the branch, run the thing, test the feature/bug fix.  The code won't pass CI/CD if it doesn't complie, but GUI features certainly will misbehave after compilation.

b) **Style**.  Follow the ![style guide](https://github.com/S010MON/project-2-2/blob/master/STYLE_GUIDE.md) and make sure it doesn't need a refactor.  Be harsh, if you can't read it now, then you certainly won't be able to understand it in a month when it's your problem!

c) **Tests**.  Check out what the tests are actually doing something, even a broken clock is right twice a day, so make sure the tests are actually useful.

![image](https://github.com/S010MON/project-2-2/blob/master/sshots/CI-CD.png)

## Merge
Finally, merge your code, delete the branch and sigh in relief.
