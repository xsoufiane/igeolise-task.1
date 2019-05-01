### Task 1 - review / safety : Solution

1. **Changes made into the review package** : 

   * **Scenario 1** - Since the blackBoxPositiveInt can side-effect and may lead to an exception, I choose to work with both the **IO Monad** of the Cats Effect and **Either**. I have implemented a generic function in [Util(src/main/scala/review/Util.scala) to respect the dry coding principle. This function constructs an 
**EitherT[IO, Exception, A]** instance and takes as a parameter a *call-by-name* value (this way we assure that the exceptions are caught within the Try). Note that thanks to Cats IO, the recursive function is trampolined automatically, which means that we are safe from the StackOverflowException. As for the exceptions, we catch only non fatal ones. Additional changes include :

     * The helper function is used only as a helper for the process function. This is why it should be hidden inside of it.
     * == must be change to <= because in the final call blackBoxPositiveInt may be greater than the total value.

   * **Scenario 2** - Database operations will surely lead to side effects and errors. Hence, I handle side effects and errors via IO and Either.
   
2. **Changes made into the design package** :

The file [Scenario3](src/main/scala/design/Scenario3.scala) contains the User trait and implementations of the FreeUser and PaidUser. I followed functional programming principles, fields are immutable and a method call does not mutate the object but creates a modified copy of it. As for the midnight logic, I have written a generic function in the UserLogic file [UserLogic](src/main/scala/design/UserLogic.scala) that will act to different Users, assuring extensibility. Moreover, I have defined a Typeclass in the [UserProvider](src/main/scala/design/UserProvider.scala) for Users. The type class acts as a factory for users and defines the generic **update** function required for the midnight logic.




 
