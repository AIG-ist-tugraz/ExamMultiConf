# ExamMultiConf

Source code to accompany the paper "Solving Multi-Configuration Problems: A Performance Analysis with Choco Solver"
as presented in [ConfWS'23](https://confws.github.io/2023/).

## Example usage

To set up a multi exam configuration you need to create a new
`ExamConfiguration` instance first. Its constructor takes two parameters:
- `examCount` - the number of exams in the configuration
- `questions` - the question pool consisting of all questions available in the configuration

`ExamConfiguration` provides several functions to add intra-/inter-exam constraints to the configuration.
(see [ExamConfiguration.java](src/main/java/at/tugraz/ist/ase/exammulticonf/configuration/ExamConfiguration.java))

Use the function `hasNextSolution()` to check with the help of Choco if another solution exists. 
If so, a call to `getConfigurations()` will return a list of all instantiated exams. 

Sample code:

```java
int numberOfExams = ...
List<Question> questionPool = ...
        
ExamConfiguration examConfiguration = new ExamConfiguration(numberOfExams, questionPool);

//Intra-exam constraint: each exam must include the question with ID 1 exactly once
examConfiguration.addGlobalQuestionConstraint(
        new QuestionScope(ExamPropagatorScope.EXACT, 1),
        new QuestionConstraintDefinition(QuestionProperty.ID, "=", 1));
        
//check if Choco finds a solution
if (examConfiguration.hasNextSolution())
{
    List<Exam> exams = examConfiguration.getConfigurations();
    //...
}
```

For a fully-fledged example that shows how to create questions,
add different constraint types and work with instantiated solutions, 
please see [Main.java](src/main/java/Main.java).