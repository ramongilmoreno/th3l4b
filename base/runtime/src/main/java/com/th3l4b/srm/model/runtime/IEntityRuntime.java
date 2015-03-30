package com.th3l4b.srm.model.runtime;

import com.th3l4b.common.data.INamedPropertied;
import com.th3l4b.common.data.named.IContainer;

public interface IEntityRuntime extends INamedPropertied,
		IContainer<IFieldRuntime> {
	IInstance create() throws Exception;

	/**
	 * Makes target an exact copy of source
	 */
	void copy(IInstance source, IInstance target) throws Exception;

	/**
	 * Puts set fields from source into target. Unset fields in source do not
	 * overwrite target.
	 */
	void apply(IInstance source, IInstance target) throws Exception;

	void unSetNulls(IInstance entity) throws Exception;
}
