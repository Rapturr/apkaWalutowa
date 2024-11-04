import { Component } from '@angular/core';
import { CurrencyService } from '../../services/currency.service';

@Component({
  selector: 'app-currency-form',
  templateUrl: './currency-form.component.html',
  styleUrl: './currency-form.component.css'
})
export class CurrencyFormComponent {
  currency: string = '';
  name: string = '';
  result: number | null = null;
  errorMessage: string | null = null;

  constructor(private currencyService: CurrencyService) {}

  getCurrencyValue() {
    this.currencyService.getCurrencyValue(this.currency, this.name)
      .subscribe({
        next: (response) => {
          this.result = response.value;
          this.errorMessage = null;
        },
        error: (error) => {
          this.result = null;
          this.errorMessage = 'Nie znaleziono waluty lub wystąpił błąd.';
        }
      });
  }
}
