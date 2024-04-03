package org.acme.concurrency;

import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;

@ApplicationScoped
public class ConcurrencyTracker {

	InternalTracker tracker = new InternalTracker();

	@Channel("concurrency")
	@OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 5000)
	MutinyEmitter<Tracker> emitter;

	private static class InternalTracker {

		private long requests;
		private long current = 0;
		private long max = 0;


		synchronized Tracker inc() {
			requests++;
			current++;
			if (current > max) {
				max = current;
			}
			return Tracker.create(this);
		}

		synchronized Tracker dec() {
			current--;
			return Tracker.create(this);
		}
	}

	public record Tracker(long requests, long current, long max) {
		static Tracker create(InternalTracker c) {
			return new Tracker(c.requests, c.current, c.max);
		}
	}

	public void inc() {
		var c = tracker.inc();
		emitter.sendAndAwait(c);
	}

	public void incAsync() {
		var c = tracker.inc();
		emitter.sendAndForget(c);
	}

	public void dec() {
		var c = tracker.dec();
		emitter.sendAndAwait(c);
	}

	public void decAsync() {
		var c = tracker.dec();
		emitter.sendAndForget(c);
	}

}
