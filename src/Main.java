import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import zserio.runtime.ZserioError;
import zserio.runtime.array.ObjectArray;
import zserio.runtime.io.FileBitStreamReader;
import zserio.runtime.io.FileBitStreamWriter;

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
        joe.setAge((short) 32);
        joe.setName("Joe Smith");
        joe.setSalary(5000);

        /* set an enum value, in this case the role */
        joe.setRole(Role.DEVELOPER);

        /* declare a list which holds a zserio struct */
        final List<Experience> skills = new ArrayList<Experience>();

        /* declare and fill the struct Experience */
        Experience skill1 = new Experience((byte) 8, Language.CPP);
        skills.add(skill1);

        /* fill a second one... */
        Experience skill2 = new Experience((byte) 4, Language.PYTHON);
        skills.add(skill2);

        /* assign the zserio object array to object joe */
        joe.setSkills(new ObjectArray<Experience>(skills));

        /* declare a zserio FileBitStreamWriter */
        final FileBitStreamWriter writer = new FileBitStreamWriter(employeeFile);

        /* serialize the object joe by passing the BitStreamWriter to its write() method */
        joe.write(writer);
        writer.close();
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

        /* declare a zserio FileBitStreamWriter */
        final FileBitStreamWriter writer = new FileBitStreamWriter(employeeFile);

        /* serialize the object boss by passing the BitStreamWriter to its write() method */
        boss.write(writer);
        writer.close();
    }

    private static void readEmployee(File employeeFile) throws IOException
    {
        /* declare the zserio FileBitStreamReader and assign the file to read from */
        final FileBitStreamReader reader = new FileBitStreamReader(employeeFile);

        /* deserialize the stream to an Employee class */
        final Employee employee = new Employee();
        employee.read(reader);

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
