import {LitElement, html, css} from 'lit';
import '@vaadin/icon';
import '@vaadin/button';
import '@vaadin/text-field';
import '@vaadin/text-area';
import '@vaadin/form-layout';
import '@vaadin/progress-bar';
import '@vaadin/checkbox';
import {until} from 'lit/directives/until.js';
import '@vaadin/grid';
import {columnBodyRenderer} from '@vaadin/grid/lit.js';
import '@vaadin/grid/vaadin-grid-sort-column.js';

export class ConcurrencyData extends LitElement {
    static styles = css`
      .concurrency-data {
        margin-right: 5px;
        margin-bottom: 5px;
        font-weight: bold;
      }
    `;

    static properties = {
        _requests: {state: true, type: Number},
        _current: {state: true, type: Number},
        _max: {state: true, type: Number}
    }

    constructor() {
        super();
        this._requests = 0;
        this._current = 0;
        this._max = 0;
    }

    render() {
        return html`
            <div class="concurrency-data">
                <p>Number of requests: ${this._requests}</p>
                <p>Current concurrency: ${this._current}</p>
                <p>Max concurrency: ${this._max}</p>
            </div>`;
    }

    connectedCallback() {
        super.connectedCallback();
        const source = new EventSource("/concurrency");
        source.onmessage = ev => {
            const t = JSON.parse(ev.data);
            this._requests = t.requests;
            this._current = t.current;
            this._max = t.max;
        }
    }

}

customElements.define('concurrency-data', ConcurrencyData);