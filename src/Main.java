import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import zserio.runtime.ZserioError;
import zserio.runtime.io.SerializeUtil;

import tutorial.Employee;
import tutorial.Language;
import tutorial.Role;
import tutorial.Experience;

public class Main
{
    private static void printHelp()
    {
        System.out.println("Usage: Main write_joe|write_boss|read");
    }

    private static void writeJoe(File employeeFile) throws ZserioError, IOException
    {
        /* declare an employee */
        final Employee joe = new Employee();

        /* fill some basic type fields */
        joe.setAge((short)32);
        joe.setName("Joe Smith");
        joe.setSalary(5000);

        /* set an enum value, in this case the role */
        joe.setRole(Role.DEVELOPER);

        /* declare an array which holds a zserio struct */
        final Experience skills[] = new Experience[] {
                new Experience((byte) 8, Language.CPP),
                new Experience((byte) 4, Language.PYTHON)
        };

        /* assign the zserio object array to object joe */
        joe.setSkills(skills);

        /* serialize the object joe to the file */
        SerializeUtil.serializeToFile(joe, employeeFile);
    }

    private static void writeBoss(File employeeFile) throws ZserioError, IOException
    {
        /* declare an employee */
        final Employee boss = new Employee();

        /* fill some basic type fields */
        boss.setAge((short) 43);
        boss.setName("Boss");
        boss.setSalary(9000);

        /* set an enum value, in this case the role */
        boss.setRole(Role.TEAM_LEAD);

        /* no programming skills for the boss, but a bonus! */
        boss.setBonus(10000);

        /* serialize the object boss to the file */
        SerializeUtil.serializeToFile(boss, employeeFile);
    }

    private static void readEmployee(File employeeFile) throws IOException
    {
        /* deserialize the file to an Employee class */
        final Employee employee = SerializeUtil.deserializeFromFile(Employee.class, employeeFile);

        /* print out the contents of Employee */
        System.out.println("Name: " + employee.getName());
        System.out.println("Age: " + employee.getAge());
        System.out.println("Salary: " + employee.getSalary());
        System.out.println("Role: " + employee.getRole());

        /* we have to check for optionals whether they are in the stream */
        if (employee.isBonusUsed())
            System.out.println("Bonus: " + employee.getBonus());

        /* we also have to check for conditions if they applied */
        if (employee.isSkillsUsed())
        {
            for (Experience experience : employee.getSkills())
            {
                final byte years = experience.getYearsOfExperience();
                final Language language = experience.getProgrammingLanguage();
                System.out.println("Skill: Language " + language + ", " + years + " years");
            }
        }

        /* print out bit size */
        System.out.println("Bit size of employee: " + employee.bitSizeOf());
    }

    public static void main(String[] args)
    {
        if (args.length != 1)
        {
            printHelp();
            System.exit(2);
        }

        final File employeeFile = new File("employee.zsb");
        try
        {
            if (args[0].equals("write_joe"))
            {
                /* writing an employee 'Joe' to file */
                writeJoe(employeeFile);
            }
            else if (args[0].equals("write_boss"))
            {
                /* writing an employee 'Boss' to file */
                writeBoss(employeeFile);
            }
            else if (args[0].equals("read"))
            {
                /* reading an employee from file */
                readEmployee(employeeFile);
            }
            else
            {
                printHelp();
                if (!args[0].equals("-h") && !args[0].equals("--help"))
                    System.exit(2);
            }
        }
        catch (ZserioError e)
        {
            System.out.println("ZserioError caught: " + e.getMessage());
            System.exit(1);
        }
        catch (IOException e)
        {
            System.out.println("IOException caught: " + e.getMessage());
            System.exit(1);
        }
    }
};
