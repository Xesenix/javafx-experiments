package pl.xesenix.experiments.experiment01.views.persons;

public interface IPersonDetailsMediator
{
	void setName(String name);

	
	void setAge(Integer age);


	void infoMessage(String message);


	void errorMessage(String message);
	
	
	void invalidAge();
	
	
	void updatePersons();
}
