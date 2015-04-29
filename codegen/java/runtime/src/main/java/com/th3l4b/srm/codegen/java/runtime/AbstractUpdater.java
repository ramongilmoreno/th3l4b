package com.th3l4b.srm.codegen.java.runtime;

import java.util.ArrayList;
import java.util.Collection;

import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IFinder;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IModelRuntime;
import com.th3l4b.srm.model.runtime.IUpdater;

public abstract class AbstractUpdater implements IUpdater {

	protected abstract IModelRuntime model() throws Exception;

	protected abstract IFinder finder() throws Exception;

	/**
	 * Called at the first change ({@link #update(IInstance, IInstance)} or
	 * {@link #insert(IInstance)}) triggered in an {@link #update(Collection)}
	 * call.
	 * 
	 * Empty (no transaction control) as per this base implementation.
	 * 
	 * After this method is called, either {@link #endTransaction()} or
	 * {@link #rollbackTransaction()} will be called.
	 */
	protected void beginTransaction() throws Exception {
	}

	/**
	 * Called at the end of changes ({@link #update(IInstance, IInstance)} or
	 * {@link #insert(IInstance)}) in an {@link #update(Collection)} call. Only
	 * called if any change was made.
	 * 
	 * Empty (no transaction control) as per this base implementation.
	 */
	protected void endTransaction() throws Exception {
	}

	/**
	 * Called at the end of changes ({@link #update(IInstance, IInstance)} or
	 * {@link #insert(IInstance)}) in an {@link #update(Collection)} call. Only
	 * called if any change was made and some error happened at commit.
	 * 
	 * Empty (no transaction control) as per this base implementation.
	 */
	protected void rollbackTransaction() throws Exception {
	}

	@Override
	public Collection<IInstance> update(Collection<IInstance> entities)
			throws Exception {
		boolean inTransaction = false;
		boolean transactionEnded = false;
		try {
			ArrayList<IInstance> r = new ArrayList<IInstance>();
			for (IInstance e : entities) {
				if (!e.coordinates().getStatus().isAction()) {
					continue;
				}
				IInstance found = finder()
						.find(e.coordinates().getIdentifier());
				if (found.coordinates().getStatus() == EntityStatus.Unknown) {
					found = null;
				}

				// Decide result
				EntityStatus newStatus = null;
				EntityStatus action = e.coordinates().getStatus();
				switch (action) {
				case ToSave:
					newStatus = EntityStatus.Saved;
					break;
				case ToDelete:
					newStatus = EntityStatus.Deleted;
					break;
				case ToMerge:
					if ((found != null)
							&& (found.coordinates().getStatus() == EntityStatus.Deleted)) {
						newStatus = EntityStatus.Deleted;
					} else {
						newStatus = EntityStatus.Saved;
					}
					break;
				default:
					throw new IllegalStateException(
							"Do not know how to handle status: " + action);
				}

				// Create a copy and apply changes
				IEntityRuntime er = model().get(
						e.coordinates().getIdentifier().getType());
				IInstance copy = er.create();
				if (found != null) {
					er.copy(found, copy);
				}
				er.apply(e, copy);
				er.unSetNulls(copy);
				copy.coordinates().setStatus(newStatus);

				// Prepare to return a copy of the copy
				IInstance copy2 = er.create();
				er.copy(copy, copy2);

				inTransaction = true;
				if (found == null) {
					insert(copy);
				} else {
					update(copy, found);
				}

				r.add(copy2);
			}

			if (inTransaction) {
				endTransaction();
				transactionEnded = true;
			}
			return r;
		} finally {
			if (inTransaction && !transactionEnded) {
				rollbackTransaction();
			}
		}
	}

	protected abstract void insert(IInstance entity) throws Exception;

	protected abstract void update(IInstance newEntity, IInstance oldEntity)
			throws Exception;

}
