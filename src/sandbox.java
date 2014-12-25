public class sandbox
{
	/*
	ArrayList<Module[]> bestSchedule = new ArrayList<Module[]>();
	String[] scheduleReadable = new String[500];
	
	double bestScore = -999;
	int numberOfLegalSchedules = 1;
	int countOfBestSchedule = 0;
	
	int countOfExecutions = 0;
	StringBuilder[] bestLog = new StringBuilder[3000];
	
	Course[] takingThisSem = new Course[] { CS225, CS233, MATH415, MATH461 };

	ArrayList<Module[]> allLegalSchedules = new ArrayList<Module[]>();	
	allLegalSchedules.add(build.buildCourseSchedule(takingThisSem));
	
	long initialTime = System.currentTimeMillis();
	//while (numberOfLegalSchedules < 438000)
	//while (System.currentTimeMillis() - initialTime < 10000)
	{
		countOfExecutions++;

		Module[] scheduleForSemester = build.buildCourseSchedule(takingThisSem);
		
		// Checks whether generated schedule is already in the list of best schedules
		boolean repeat = true;
		while (repeat == true)
		{
			for (int i = 0; i < countOfBestSchedule; i++)
			{
				if (help.compareModuleArrays(scheduleForSemester, bestSchedule.get(i)))
				{
					scheduleForSemester = build.buildCourseSchedule(takingThisSem);
					i = -1;
				}
				//if (i == countOfBestSchedule-1)
				//	repeat = false;
			}
			repeat = false;
		}
		
		//This code counts the number of legal schedules (without clashes)
		for (int i = 0;i<numberOfLegalSchedules;i++)
		{
			if (System.currentTimeMillis() - initialTime < 120000)
			{
				if (help.compareModuleArrays(scheduleForSemester, allLegalSchedules.get(i)))
				{
					scheduleForSemester = build.buildCourseSchedule(takingThisSem);
					i = -1;
					//System.out.println("Reset here");	
				}
			}
		} 
		allLegalSchedules.add(scheduleForSemester);
		//System.out.println(help.toString(allLegalSchedules[numberOfLegalSchedules]));
		

		numberOfLegalSchedules++;
		Module[][] finalSchedule = help.convertModuleArrayToSchedule(scheduleForSemester);

		double currentScore = (scorer.scoreSchedule(finalSchedule));
		//System.out.println(currentScore);
		if (currentScore > bestScore)
		{
			bestSchedule.clear();
			scheduleReadable = new String[200];
			bestLog = new StringBuilder[200];
			countOfBestSchedule = 0;
			
			bestLog[countOfBestSchedule] = new StringBuilder(scorer.log);
			bestSchedule.add(help.deepCopyModuleArray(scheduleForSemester));
			scheduleReadable[countOfBestSchedule] = help.toString(finalSchedule);
			bestScore = currentScore;
			
			countOfBestSchedule++;
		} 
		else if (currentScore == bestScore)
		{
			{
				bestLog[countOfBestSchedule] = new StringBuilder(scorer.log);
				//bestSchedule[countOfBestSchedule] = help.deepCopyModuleArray(scheduleForSemester);
				bestSchedule.add(help.deepCopyModuleArray(scheduleForSemester));
				scheduleReadable[countOfBestSchedule] = help.toString(finalSchedule);
				countOfBestSchedule++;
			}
		}
	}
	
	System.out.println("Found " + (countOfBestSchedule) + " optimal schedules\n");
	for (int i = 0; i < countOfBestSchedule; i++)
	{
		System.out.println(scheduleReadable[i]);
		System.out.println(bestLog[i]);
		System.out.println("--------------------------------------------------------------------------------");
		
	}

	System.out.println("Score: " + bestScore);
	System.out.println("Number of runs: " + numberOfLegalSchedules);
	System.out.println("Runtime: "+ ((System.currentTimeMillis() -initialTime)/Double.valueOf(1000)) + " seconds");

	*/
	
	
	//System.out.println(System.currentTimeMillis()-initialTime);
			/*
			for (int i = 0;i<allSchedules.size();i++)
			{
				loop:
				for (int j = 0;j<allSchedules.get(i).length;j++)
				{
					for (int k = j+1;k<allSchedules.get(i).length;k++)
					{
						if (j != k && help.checkConflict(allSchedules.get(i)[j], allSchedules.get(i)[k]))
						{
							allSchedules.remove(i);
							i--;
							break loop;
						}
					}
				}
			
			}
			System.out.println(System.currentTimeMillis()-initialTime);
			*/
			
	public static void main(String[] args)
	{
	
	}
}
