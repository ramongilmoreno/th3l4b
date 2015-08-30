
module Base {

    export interface INamed {
        name: string;
    }

    export interface IProperties {
        [index: string]: string;
    }

    export interface IAttributes {
        [index: string]: any;
    }

    export interface IPropertied {
        properties: IProperties;
        attributes: IAttributes;
    }

    export interface INamedPropertied extends INamed, IPropertied { }

    export class DefaultPropertied implements IPropertied {
        _properties: IProperties = {};
        _attributes: IAttributes = {};
        get properties(): IProperties { return this._properties; }
        get attributes(): IAttributes { return this._attributes; }
    }

    export class DefaultNamed extends DefaultPropertied implements INamed {
        private _name: string;
        set name(name: string) { this._name = name; }
        get name(): string { return this._name }
    }

    export interface IContainer<T extends INamed> {
        find(name: string): T;
        add(named: T): void;
        remove(name: string): void;
        contains(name: string): boolean;
        size(): number;
    }

    export class DefaultContainer<T extends INamed> extends DefaultPropertied implements IContainer<T> {
        private _items = {};
        private _list = [];
        private key(name: string): string { return "key-" + name; }
        find(name: string): T { return this._items[this.key(name)]; }
        add(named: T): void { this._items[this.key(named.name)] = named; this._list.push(name); }
        remove(name: string): void {
            var key: string = this.key(name);
            delete this._items[key];
            var index = this._list.indexOf(key, 0);
            if (index != undefined) {
                this._list.splice(index, 1);
            }
        }
        contains(name: string): boolean { return this._list.indexOf(this.key(name), 0) != undefined; }
        size(): number { return this._list.length; }
    }

    export interface IInstance { }
}