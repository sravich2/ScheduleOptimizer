import java.util.ArrayList;

public class CourseBuilder
{

	Worker help = new Worker();

	public Meeting[] buildCourseSchedule(Course[] coursesTaken)
	{
		while (true)
		{
			ArrayList<Meeting> finalClasses = new ArrayList<Meeting>();

			for (int i = 0; i < coursesTaken.length; i++)
			{
				ArrayList<Meeting> modulesForThisCourse = new ArrayList<Meeting>();
				modulesForThisCourse = (help.chooseRandomModules(coursesTaken[i]));
				finalClasses.addAll(modulesForThisCourse);
			}

			String output = "";
			int size;
			size = finalClasses.size();
			
			outerloop: 
			for (int i = 0; i < size; i++)
			{
				for (int j = 0; j < size; j++)
				{
					if ((i != j) && (help.checkConflict(finalClasses.get(i), finalClasses.get(j))))
					{
						break outerloop;
					}
					if (i == size - 1 && j == size - 1)
					{
						//output = (help.toString(finalClasses.toArray(new Module[size])));
						//System.out.println("FOUND: \n"+ output);
						return finalClasses.toArray(new Meeting[size]);
					}
				}
			}
		}
		//	return "Didn't get anything";
	}
	
	public Meeting[] geneticBuild(Meeting[][] population)
	{
		return null;
	}
	
	public Meeting[][] generatePopulation(Course[] coursesTaken)
	{
		final int POPULATION_SIZE = 500;
		ArrayList<Meeting[]> population = new ArrayList<Meeting[]>(POPULATION_SIZE);
		int count = 0;
		while (count < 1000)
		{
			Meeting[] prospectiveSchedule = this.buildCourseSchedule(coursesTaken);
			for (int i = 0;i<count;i++)
			{
				if (help.compareModuleArrays(prospectiveSchedule, population.get(i)))
				{
					prospectiveSchedule = this.buildCourseSchedule(coursesTaken);
					i = -1;
				}
			}
			population.add(prospectiveSchedule);
		}
		return population.toArray(new Meeting[1000][]);
	}
	
	public Meeting[] geneticMutate(Meeting[] inputSchedule)
	{
		return null;
	}

}
