/// <reference path="Base"/>

module Model {

    export interface IFieldRuntime extends Base.INamedPropertied {
        get(): string;
        set(value: string, instance: IInstance);
        isSet(instance: IInstance): boolean;
        unSet(instance: IInstance): void;
    }

    export interface IEntityRuntime extends Base.INamedPropertied, Base.IContainer<IFieldRuntime> {
        create(): IInstance;
        copy(source: IInstance, target: IInstance): void;
        apply(source: IInstance, target: IInstance): void;
        subtract(source: IInstance, target: IInstance): void;
        unSetNulls(entity: IInstance): void;
    }

    export interface IModelRuntime extends Base.INamedPropertied, Base.IContainer<IEntityRuntime> {
    }
}