package com.th3l4b.srm.sync.base;

import java.util.Collection;
import java.util.HashMap;

import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.ICoordinates;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IIdentifier;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IModelRuntime;

public class SyncUtils {

	/**
	 * Groups multiples updates into a single one.
	 * 
	 * @param input
	 *            The list of updates. May contain more than one update for the
	 *            same entity.
	 * @return The list of updates, grouped by entity. No more than an update
	 *         per entity may exist in this result.
	 */
	public static Collection<IInstance> groupUpdates(
			Collection<IInstance> input, IModelRuntime mr) throws Exception {
		HashMap<IIdentifier, IInstance> r = new HashMap<IIdentifier, IInstance>();
		for (IInstance instance : input) {
			ICoordinates coordinates = instance.coordinates();
			EntityStatus status = coordinates.getStatus();
			if (!status.isAction()) {
				continue;
			}
			IIdentifier id = coordinates.getIdentifier();
			IEntityRuntime er = mr.get(id.getType());
			if (!r.containsKey(id)) {
				IInstance copy = er.create();
				er.copy(instance, copy);
				r.put(id, copy);
			} else {
				IInstance found = r.get(id);
				EntityStatus initialStatus = found.coordinates().getStatus();

				// Merge
				er.apply(instance, found);

				EntityStatus resultStatus = null;
				if ((status == EntityStatus.ToDelete)
						|| (status == EntityStatus.ToSave)) {
					resultStatus = status;
				} else {
					resultStatus = initialStatus;
				}
				found.coordinates().setStatus(resultStatus);
			}
		}
		return r.values();
	}
}
