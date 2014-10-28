package com.epam.concurrency;

import java.util.concurrent.CountDownLatch;

import com.epam.concurrency.services.PersonService;

public class PersonGenerator implements Runnable {
	
	private static final PersonService service = new PersonService();

	private final CountDownLatch latch;

	public PersonGenerator(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {
		int count = service.getList().size();
		while(true && !Thread.currentThread().isInterrupted()) {
			if(count >= 5) {
				latch.countDown();
				Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
			}
			String firstName = "FirstName" + count;
			String lastName = "LastName" + count;
			if(service.addPerson(firstName, lastName)) {
				count++;
			}

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}