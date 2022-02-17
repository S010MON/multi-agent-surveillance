# Style Guide
This style guide is to help as a reference for code review (both for those making PRs and reviewing them) and to ensure consistency across the project code base. It is based off the ![Google Style Guide](https://google.github.io/styleguide/javaguide.html) which is highly recommended reading, as it includes far more detail.  None of the style guide is set in stone, if disagree with the notes here, please suggest changes with a pull request from a new `style-guide` branch.

## 0 Contents
##### ![1 - Source Code Structure](https://github.com/S010MON/temp/blob/main/STYLE_GUIDE.md#1-source-code-structure)
- ![1.1 - Wildcard Imports](https://github.com/S010MON/temp/blob/main/STYLE_GUIDE.md#11-wildcard-imports)
- ![1.2 - Ordering Imports](https://github.com/S010MON/temp/blob/main/STYLE_GUIDE.md#12-ordering-imports)
- ![1.3 - Class Declaration](https://github.com/S010MON/temp/blob/main/STYLE_GUIDE.md#13-class-declaration)
- ![1.4 - Method Declaration](https://github.com/S010MON/temp/blob/main/STYLE_GUIDE.md#14-method-declaration)
- ![1.5 - Variable Declarations](https://github.com/S010MON/temp/blob/main/STYLE_GUIDE.md#15-variable-declarations)
##### ![2 - Formatting](https://github.com/S010MON/temp/blob/main/STYLE_GUIDE.md#2-formatting)
- ![2.1 - Brackets](https://github.com/S010MON/temp/blob/main/STYLE_GUIDE.md#21-brackets)
- ![2.2 - Empty Blocks](https://github.com/S010MON/temp/blob/main/STYLE_GUIDE.md#22-empty-blocks)
- ![2.3 - Indentation](https://github.com/S010MON/temp/blob/main/STYLE_GUIDE.md#23-indentation)
- ![2.4 - Vertical Whitespace](https://github.com/S010MON/temp/blob/main/STYLE_GUIDE.md#24-vertical-whitespace)
- ![2.5 - Horizontal Whitespace](https://github.com/S010MON/temp/blob/main/STYLE_GUIDE.md#25-horizontal-whitespace)
##### ![3 - Functional Programming](https://github.com/S010MON/temp/blob/main/STYLE_GUIDE.md#3-functional-programming)
- ![3.1 - Side Effects](https://github.com/S010MON/temp/blob/main/STYLE_GUIDE.md#31-side-effects)
- ![3.2 - Parameters](https://github.com/S010MON/temp/blob/main/STYLE_GUIDE.md#32-parameters)
##### ![4 - Lombok and Annotations]()
- ![4.1 - Lombok]()
- ![4.2 - Annotations]()

## 1 Source Code Structure
A source file consists of, in order:

1. License or copyright information, if present
2. Package statement
3. Import statements
4. Exactly one top-level class

Exactly one blank line separates each section that is present.

### 1.1 Wildcard Imports
Imports should import the class used, not the entire package. For example this:
```java
import java.awt.*;
```
Should be replaced with this:
```java
import java.awt.Color;
import java.awt.Font;
```
As importing all of the awt package pollutes the namespace; imagine we designed our own Button class, this would now be in conflict with `awt.Button` without us even knowing!

### 1.2 Ordering Imports
1. All static imports in a single block.
2. All non-static imports in a single block.
3. Imports grouped by higher level package

**Incorrect**. Note how all the imports are mixed up
```java
import javafx.application.Application;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import java.awt.Color;
import javafx.scene.Scene;
import java.awt.Font;
import javafx.stage.Stage;
```
**Correct**
```java
import java.awt.Font;                    // Order alphabetically if possible
import java.awt.Color;
import java.io.IOException;
import javafx.application.Application;   // No splits between high level packages
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
```

### 1.3 Class Declaration
Each file should have only *one* top level class (or Interface/Enum) which should be the same name as the file.  If you design a sub class, it should be testable and reusable in the rest of the code base. Classes should be named in `UpperCamelCase`.

### 1.4 Method Declaration
Methods should be ordered in a sensible way, there is no hard and fast rule for this, however it is suggested that common sense is applied such that if asked, *some* sort of ordering can be explained.  A good guide can be found in the book Clean Code with a brief example below:
```java
public class MyClass
{
    public MyClass()
    {
        // Constructor always first
    }

    public String keyMethod()
    {
        // This is the main job of the class; the user should see this right away
        secondaryMethod()
    }

    public void secondaryMethod() {...} // This is called above, so makes sense to have 
                                        // it right below where it is called

    public int getAttribute(){...}  // All public getters and setters together

    public void setAttribute(int n){...}

    private void doSomething(){...}  // Private methods are where we do internal implementation that 
                                     // users shouldn't worry about; thus last
}
```
If your ordering method is chonological ( *i.e. "I just add it in at the bottom"* ) then go look in the mirror and think about what you have done ...

### 1.5 Variable Declarations
1. Every variable declaration (field or local) declares only one variable: declarations such as `int a, b;` are not used.
2. Variable are named in `lowerCamelCase` in the case of local or field varaibles
3. Constant variables (when the `final` keyword is used) should be in `CONSTANT_CASE`
4. Local variables are not habitually declared at the start of their containing block or block-like construct. Instead, local variables are declared close to the point they are first used (within reason), to minimize their scope. Local variable declarations typically have initializers, or are initialized immediately after declaration.
5. Class variables should, wherever possible be private and modified using a getter and setter method


## 2 Formatting
I don't want to start any wars, so formatting will not be used as a reason to stop a pull request.  **However**, all methods in the same class **MUST** use the same format.  So if you add a single method to a class, either follow the format of the class, or reformat the whole class to your style!

### 2.1 Brackets
For an in depth view of the different indentation styles, see ![here](https://en.wikipedia.org/wiki/Indentation_style), I personally prefer the Allman style of braces as I believe that this is clearer to read:
```java
while(x == y)
{
    doSomething();
    doSomethingElse();
}
```
This is due to the addition of multiple arguments:
```java
void MyFunction(int parameterOne,
                int parameterTwo) {     // This is not clearly a second argument
    Class localOne = new Class();
    Class localTwo = new Class();
}
```
And the ease of identifying the contents of a block when nesting as this:
```java
if (you.hasAnswer())
{
    if(x == y)
    {
        you.postAnswer();
    }
}
else
{
    you.doSomething();
}
```
Is far more readable than:
```java
if (you.hasAnswer()) {
    if(x == y){
        you.postAnswer();
    }
} else {
    you.doSomething();
}
```

### 2.2 Empty Blocks
Should be terminated on the same line.
```java
private void doNothing(){}
```

### 2.3 Indentation
+4 spaces for each indented block, the level of indentation should be reduced as much as possible through the use of sub-methods so that the example below
```java
public void doSomething(int n)
{
    for(int i = 0; i < n; i++)
    {
        if(i % 2 == 0)
        {
            try
            {
                doSomethingElse();
                {
                    ...
                }
            }
            catch(Exception e)
            {
                ...
            }
        }
    }
}
```
should be refactored into seperate methods like this:
```java
public void tryToDoSomthing(int n)
{
    try
    {
        doSomething(int n);
    }
    catch(Exception e)
    {
        // handle exception
    }
}

public void doSomething(int n) throws Exception
{
    for(int i = 0; i < n; i++)
    {
        doSomethingIfEven(i);
    }
}

private void doSomethingIfEven(int n)
{
    if(i % 2 == 0)
    {
        doSomethingElse();
    }
}
```

### 2.4 Vertical Whitespace
A single blank line should be left between the closing backets of a method and the opening declaration of a new method.
Correct:
```java
public void methodOne()
{
    ...
}

public void methodTwo()
{
```
Incorrect:
```java
public void methodOne()
{
    ...
}
public void methodTwo() // WRONG!
{
```
A single blank line may also appear anywhere it improves readability, for example between statements to organize the code into logical subsections. A blank line before the first member or initializer, or after the last member or initializer of the class, is neither encouraged nor discouraged.

### 2.5 Horizontal Whitespace
Beyond where required by the language or other style rules, and apart from literals, comments and Javadoc, a single ASCII space also appears in the following places only.

1. Separating any reserved word, such as `if`, `for` or `catch`, from an open parenthesis `(` that follows it on that line
2. Separating any reserved word, such as `else` or `catch`, from a closing curly brace `}` that precedes it on that line
3. Before any open curly brace `{`, with exception:
   `String[][] x = {{"foo"}};  //no space is required between {{`
4. On both sides of any binary operator.
   `a + b` or `int i = 0;` or `if(a && b)`
5. On both sides of the double slash `//` that begins an end-of-line comment.
   `addComment() // note the space on each side!`
6. Between the type and variable of a declaration:
   `List<String> list`
7. After a comma in a list:
   `new int[] {5, 6}`

This rule is never interpreted as requiring or forbidding additional space at the start or end of a line; it addresses only interior space.

## 3 Functional Programming
The intent of functional programming is that a function, given a certain input, will always output the same result.  This reduces the amount of 'state' knowledge that needs to be held and reduces the number of side effects of the code when you call it.

### 3.1 Side Effects
When calling a method, it should be either explicitly stated the one action that occurs, or even better should return a new value instead:
```java
// BAD!
public int getNPlusOne()
{
    n++;                // Increments n, but doesn't tell the user that this happens!
    return n;
}

// Better ... 
public int incrementN()
{
    n++;                // Better naming; less suprises
    return n;
}

// Best!
public int nPlusOne()
{
    return n + 1;       // Doesn't modify the state, just returns a new local copy
}
```

### 3.2 Parameters
The number of parameters of a good function should be 0, this isn't always possible, but the ideal function gets called and always does the right thing regardless of state:
```java
public void toggle()
{
    if(b)
        b = false;
    b = true;
}
```
Avoid having large numbers of parameters, condense them into an object or ask why the function needs to know so many things?  Is it becoming too big?
```java
public int startGame(int boardSize,
                    int playerCount,
                    Player[] players,
                    Player startingPlayer,
                    Color[] colours,
                    JFrame view,
                    JPanel Board)
{...}
```
If we add all the settings into a `Settings` data class and allow the view to call the board, we reduce to two parameters
```java
public  int startGame(Settings settings, JFrame view) {...}
```

## 4 Lombok and Annotations
Lombok allows you to reduce the Boilerplate code you write and makes classes cleaner

#### 4.1 Lombok Features
The intent is that only useful code is shown to the end user of the class.  For example, creating an immutable Vector class can be done as follows:

![Screenshot from 2022-02-16 09-36-42](https://user-images.githubusercontent.com/10490509/154226684-fcf55ed9-8a81-4ef5-bfef-11c4d79a21ad.png)

Where default constructor, all args constructor and to string method are all wrapped in 6 lines of code.  Contrast this with the original:

![Screenshot from 2022-02-16 09-39-33](https://user-images.githubusercontent.com/10490509/154226970-f9b2a803-139d-400e-8170-5b48cd011935.png)

It must be remembered that Lombok is there to reduce boilerplate code that is required but completely self explainatory.  If any changes to the default implementation are made, this should be done without using the Lombok annotations

#### 4.2 Lombok Annotations
Class annotations should be listed with each on a new line:

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DataClass
{
   ...
}
```

For field variables, in line definitions are preferred:
```java
@Getter private String thisIsOkay;
@Getter @Setter private thisIsAlsoOkay;
```
However for more than two annotations, a new line should be used:
```java
@Getter @Setter @NotNull
private int number;
```
Class definitions can be used to cover all filed variables, however consider the impact of refactoring in the case of future users if the class may be partially developed

