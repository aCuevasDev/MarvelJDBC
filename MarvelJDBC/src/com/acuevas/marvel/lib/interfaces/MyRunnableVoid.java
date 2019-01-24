package com.acuevas.marvel.lib.interfaces;

/**
 * Same as MyRunnable but myRun returns void
 * 
 * @author Alex
 *
 * @param <T>
 */
@FunctionalInterface
public interface MyRunnableVoid {
	void myRun() throws Exception;
}
