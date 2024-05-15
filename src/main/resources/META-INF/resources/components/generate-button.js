import {LitElement, html, css} from 'lit';
import '@vaadin/icon';
import '@vaadin/button';
import '@vaadin/text-field';
import '@vaadin/text-area';
import '@vaadin/form-layout';
import '@vaadin/progress-bar';
import '@vaadin/checkbox';
import '@vaadin/horizontal-layout';
import {until} from 'lit/directives/until.js';
import '@vaadin/grid';
import {columnBodyRenderer} from '@vaadin/grid/lit.js';
import '@vaadin/grid/vaadin-grid-sort-column.js';

export class GenerateButton extends LitElement {
    static styles = css`
      .button {
        cursor: pointer;
      }
      vaadin-text-field {
        width: 50%;
      }
    `;

    static properties = {
        count: {type: Number},
        url: {type: String}
    }

    constructor() {
        super();
        this.count = 1000
        this.url = "/blocking"
    }

    render() {
        return html`
            <vaadin-horizontal-layout theme="spacing padding"
                                      style="align-items: baseline">
                <vaadin-button
                        arial-label="Generate"
                        @click=${this._generate} class="button primary">
                    Generate
                </vaadin-button>
                <vaadin-text-field
                        .value=${this.count}
                        @value-changed=${e => {
                            this.count = e.target.value
                        }}
                        allowed-char-pattern="[0-9]">
                </vaadin-text-field>
                <vaadin-text-field
                        .value=${this.url}
                        @value-changed=${e => {
                            this.url = e.target.value
                        }}>
                </vaadin-text-field>
            </vaadin-horizontal-layout>`;
    }

    _generate() {
        fetch(`/generate?url=${this.url}&count=${this.count}`, {
            method: "GET",
            // headers: {
            //     "Content-Type": "application/json",
            // },
        });
    }

}

customElements.define('generate-button', GenerateButton);