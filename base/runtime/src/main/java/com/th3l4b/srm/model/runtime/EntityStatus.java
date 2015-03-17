package com.th3l4b.srm.model.runtime;

/**
 * Statuses are either {@link #isAction()} or not. In the case it is an action,
 * then an update in the repository happens when fed to the
 * {@link IUpdater#update(java.util.Collection)} method. If not an action, the
 * update process will simply skip it.
 */
public enum EntityStatus {

	Saved, Deleted, Unknown, Ignore,
	/**
	 * This is identical {@link #ToSave}, but preserves the {@link #Saved} or
	 * {@link #Deleted} status if the object is exists in the repository.
	 */
	ToMerge(true),
	/**
	 * This status will persist the object in the repository and its status
	 * after applying this update will be {@link #Saved}.
	 */
	ToSave(true),
	/**
	 * This status will persist the object in the repository and its status
	 * after applying this update will be {@link #Deleted}.
	 */
	ToDelete(true);

	boolean _action = false;

	EntityStatus() {
		this(false);
	}

	EntityStatus(boolean action) {
		_action = action;
	}

	public boolean isAction() {
		return _action;
	}
}
