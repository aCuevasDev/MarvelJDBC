package com.acuevas.marvel.lib;

/**
 * Custom Runnable interface which is able to throw exceptions and return an
 * object.
 * 
 * @see Runnable
 * @author Alex
 * @param <T> the Type of the returned object from myRun.
 *
 */
@FunctionalInterface
public interface MyRunnable<T> {

	/**
	 * When an object implementing interface MyRunnable is used to create a thread,
	 * starting the thread causes the object's run method to be called in that
	 * separately executing thread. from @Runnable
	 * 
	 * @throws Exception
	 * @returns T .. An object
	 */
	T myRun() throws Exception;

}
