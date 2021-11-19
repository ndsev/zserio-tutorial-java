# Zserio Java Quick Start Tutorial

This Quick Start tutorial features code generation in Java. Go to the
[Zserio C++ tutorial](https://github.com/ndsev/zserio-tutorial-cpp#zserio-c-quick-start-tutorial) or
[Zserio Python tutorial](https://github.com/ndsev/zserio-tutorial-python#zserio-python-quick-start-tutorial) if
you are interested in hands-on C++ or Python with zserio.

You find the complete tutorial in this example. To follow along the description just clone this repo and check
the sources.

The latest build of the zserio compiler and runtime library can be get from
[Zserio Releases](https://github.com/ndsev/zserio/releases).

If you want to build from source, please follow the
[Zserio Compiler Build Instructions](https://github.com/ndsev/zserio/blob/master/doc/ZserioBuildInstructions.md#zserio-compiler-build-instructions).

## Installation & Prerequisites

Before we start, make sure you have the following components installed:

- Java JDK
- Apache Maven

## Set up dev environment

> Everything has been already set up for you in this repository. If you are very impatient, just go to the
> project's root folder and have a quick look to the schema `tutorial.zs`. Then, run the following commands:
>
> ```
> mvn dependency:copy -Dmaven.repo.local="build/download" \
>        -Dartifact=io.github.ndsev:zserio-runtime:LATEST \
>        -DoutputDirectory="build" -Dmdep.stripVersion=true
> javac -d build -cp build/zserio-runtime.jar src/Main.java src/**/*.java
> ```
>
> Now, start to play with tutorial executable using the command:
>
> `java -cp build/zserio-runtime.jar:build Main`

We start with a common layout of our project/repo where we put all the source files into a `src` folder.
For simplicity the zserio schema file stays in the project's root folder.

Now we only need to generate the code, populate the Main.java and we are done.

But before we can generate code, we need to write the schema definition of our data.

## Writing a schema

Open up your favorite text editor and start writing your schema. We will use the example from the zserio repo
plus some additional structures to showcase some of zserio's features.

```
package tutorial;

struct Employee
{
    uint8           age : age <= 65; // max age is 65
    string          name;
    uint16          salary;
    optional uint16 bonus;
    Role            role;

    // if employee is a developer, list programming skill
    Experience      skills[] if role == Role.DEVELOPER;
};

struct Experience
{
    bit:6       yearsOfExperience;
    Language    programmingLanguage;
};

enum bit:2 Language
{
    CPP     = 0,
    JAVA    = 1,
    PYTHON  = 2,
    JS      = 3
};

enum uint8 Role
{
    DEVELOPER = 0,
    TEAM_LEAD = 1,
    CTO       = 2
};
```

We have added some of zserio's features above. Let's quickly take a look:

- **Constraints**

  Although the `uint8` of field `age` would allow values up to 255, we limit the use already in the schema
  definition by using
  a [constraint](https://github.com/ndsev/zserio/blob/master/doc/ZserioLanguageOverview.md#constraints).
  If we try to write values larger than 65, the generated writers will throw an exception.

- **Optional fields**

  The `bonus` field is prefixed with the keyword `optional` which will add a invisible 1-bit bool before that
  field which indicating whether the field exists. If it is not set then only one bit will be added to the bit
  stream. See
  [Zserio Invisibles](https://github.com/ndsev/zserio/blob/master/doc/ZserioInvisibles.md#optional-keyword)
  for more information.

- **Conditions**

  We add programming skills only if the employee is developer.

- **Bit sized elements**

  The struct `Experience` uses 1 byte in total. It uses 6 bit to store the years of programming experience and
  2 bits for the enum `Language`.

For more details on the features of zserio head over to the
[Zserio Language Overview](https://github.com/ndsev/zserio/blob/master/doc/ZserioLanguageOverview.md).

We now save the file to disk as `tutorial.zs`.

> Please note that the filename has to be equivalent to the package name inside the zserio file.
> The zserio compiler accepts arbitrary file extensions (in this case `*.zs`). But make sure that all imported
> files also have the same file extension.

## Compiling and generating code

Now we are ready to compile the schema with the zserio compiler. The zserio compiler checks the schema file and
its [imported files](https://github.com/ndsev/zserio/blob/master/doc/ZserioLanguageOverview.md#packages-and-imports)
and reports errors and warnings. In addition, the zserio compiler generates code for the supported languages
and may generate HTML documentation. For a complete overview of available options, please refer to the
[Zserio Compiler User Guide](https://github.com/ndsev/zserio/blob/master/doc/ZserioUserGuide.md#zserio-compiler-user-guide).

So let's generate some Java code. Because zserio compiler is not available in this repository, we must
download the latest zserio compiler release together with corresponded Java runtime library from Maven central
repository. For example, it's enough just to run the following command:

```
mvn dependency:copy -Dmaven.repo.local="build/download" \
        -Dartifact=io.github.ndsev:zserio:LATEST \
        -DoutputDirectory="build" -Dmdep.stripVersion=true
```

After download, you can find out the latest zserio compiler in directory `build` and regenerate
the Java code by hand using the command:

```
java -jar build/zserio.jar -java src tutorial.zs
```

This command generates Java code and puts it into the `src` folder. It actually creates subfolders for each
package in the schema.

So after generating the code our folder structure looks like this:

```
.
└───src
    └───tutorial
```

Let's take a quick look what has been generated. In the `src/tutorial` folder you now find the following files:

```
Employee.java  Experience.java  Language.java  Role.java
```

There is one Java file for each struct or enum.

We now have everything ready to serialize and deserialize our data.

## Serialize using the generated code

> Note: The example code in this repository features the creation of two objects of class Employee: Joe and
> his boss. We will mostly cover the creation of Joe here.

Before we start programming, let's have compile our project:

```
javac -d build -cp build/zserio-runtime.jar src/Main.java src/**/*.java
```

Then open up your favorite IDE and start using the zserio classes by importing the classes from the schema
and zserio runtime that we want to use.

```java
import zserio.runtime.ZserioError;
import zserio.runtime.io.FileBitStreamReader;
import zserio.runtime.io.FileBitStreamWriter;

import tutorial.Employee;
import tutorial.Language;
import tutorial.Role;
import tutorial.Experience;
```

Let's declare an employee Joe and fill in some data:

```java
/* declare an employee */
final Employee joe = new Employee();

/* fill some basic type fields */
joe.setAge((short)32);
joe.setName("Joe Smith");
joe.setSalary(5000);

/* set an enum value, in this case the role */
joe.setRole(Role.DEVELOPER);
```

To be able to populate a list of skills, we need to declare and fill an array with element of type Experience.
Let's generate two entries for the skills list. First we add C++ experience and then also some Python
experience:

```java
final Experience skills[] = new Experience[] {
                new Experience((byte) 8, Language.CPP),
                new Experience((byte) 4, Language.PYTHON)
        };
```

Don't forget to set Joe's skills:

```java
joe.setSkills(skills);
```

After we have set all the fields, we have to declare a FileBitStreamWriter and write the stream to the file:

```java
final FileBitStreamWriter writer = new FileBitStreamWriter(employeeFile);

/* serialize the object joe by passing the BitStreamWriter to its write() method */
joe.write(writer);
writer.close();
```

**Voila!** You have just serialized your first data with zserio.

**Congratulations!**

## Deserialize using the generated code

We already pointed out that Joe has a boss in the code we checked in. In the deserialization code we need to
keep an eye on all possible serializations we might have to deal with. So let's quickly look at the differences
between Joe and his boss.

Joe's boss is a little older, has a higher salary, gets a bonus but has no programming skills, because our
schema definition does not allow team leads to have programming skills. ;-)

```java
/* set an enum value, in this case the role */
boss.setRole(Role.TEAM_LEAD);

/* no programming skills for the boss, but a bonus! */
boss.setBonus(10000);
```

The rest is pretty similar. Check the code to see the rest.

When deserializing the zserio bit stream, we start with reading the file using FileBitStreamReader declaration:

```java
final FileBitStreamReader reader = new FileBitStreamReader(employeeFile);
```

We declare an object of class Employee and deserialize the buffer with the help of the FileBitStreamReader we
just created. After this call all the fields within `employee` will be set.

```java
final Employee employee = new Employee();
employee.read(reader);
```

We can now access the filled employee object via the respective getters. We still need to check for optionals
and conditionals whether they have been set.

```java
/* print out the contents of Employee */
System.out.println("Name: " + employee.getName());
System.out.println("Age: " + employee.getAge());
System.out.println("Salary: " + employee.getSalary());
System.out.println("Role: " + employee.getRole());

/* we have to check for optionals whether they are in the stream */
if (employee.isBonusUsed())
    System.out.println("Bonus: " + employee.getBonus());
```

For the rest of the processing please refer to the code. You should have gotten the main point by now.

## Additions you will find in the code

There are some other features that we used in the code in this repo that we would like to point out briefly:

- zserio runtime exception handling
- some zserio API calls

### Zserio runtime exceptions

The zserio runtime throws two exceptions. The `zserio.runtime.ZserioError` and the `java.io.IOException`.

It makes sense to try-catch all of your writes and reads as we do in our tutorial:

```java
try
{
    // read or write
}
catch (ZserioError e)
{
    System.out.println("ZserioError caught: " + e.getMessage());
}
catch (IOException e)
{
    System.out.println("IOException caught: " + e.getMessage());
}
```

Examples for when an exception will be thrown:

- **Data type range exceptions**

  Zserio types get mapped to Java native types of a bigger type sometimes (e.g. `bit:2` to `short`). You may
  assign values that fit into the Java native type which will compile fine, but the zserio runtime will throw
  an exception if it does not fit into the zserio schema.

  > Example: Try to give Joe a programming experience of 100 years.

- **Constraint exceptions**

  If there is a constrain in the schema that requires a certain field to be set to specific value, the
  zserio runtime will throw an exception if you try to set the field without the constraint being met.

  > Example: Try to make Joe 100 years old.

### Zserio API calls

The example uses one smaller feature that we would like to explain.

The feature is that you can always retrieve the actual bit size of the structures in zserio by calling
`bitSizeOf()`.

In the tutorial we use it for plain informational purpose only.

```java
System.out.println("Bit size of employee: " + employee.bitSizeOf());
```
