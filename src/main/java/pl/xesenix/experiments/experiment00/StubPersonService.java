package pl.xesenix.experiments.experiment00;

import java.util.ArrayList;
import java.util.List;

import pl.xesenix.experiments.Person;
import pl.xesenix.experiments.Skill;

public class StubPersonService implements IPersonService
{
	List<Person> persons;
	
	
	public StubPersonService()
	{
		Skill karate = new Skill("karate");
		Skill programming = new Skill("programming");
		Skill walking = new Skill("walking");

		Person adam = new Person("Adam");
		adam.setAge(12);
		adam.getSkills().add(walking);
		adam.getSkills().add(programming);

		Person ewa = new Person("Ewa");
		ewa.getSkills().add(karate);

		Person pawel = new Person("Paweł");
		pawel.getSkills().add(karate);
		pawel.getSkills().add(programming);
		pawel.getSkills().add(walking);
		
		persons = new ArrayList<Person>();
		persons.add(adam);
		persons.add(ewa);
		persons.add(pawel);
	}

	public List<Person> getPersons()
	{
		return persons;
	}

	public Person getPersonDetails(String name)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void acceptPerson(Person person)
	{
		// TODO Auto-generated method stub
		
	}

	public void declinePerson(Person person, String Reason)
	{
		// TODO Auto-generated method stub
		
	}

}
