import { Component, OnInit } from '@angular/core';
import { CurrencyService } from '../../services/currency.service';

@Component({
  selector: 'app-currency-requests',
  templateUrl: './currency-requests.component.html',
  styleUrls: ['./currency-requests.component.css']
})
export class CurrencyRequestsComponent implements OnInit {
  requests: any[] = [];

  constructor(private currencyService: CurrencyService) {}

  ngOnInit(): void {
    this.getAllRequests();
  }

  getAllRequests() {
    this.currencyService.getAllRequests()
      .subscribe({
        next: (data) => this.requests = data,
        error: (error) => console.error('Error fetching requests', error)
      });
  }
}
