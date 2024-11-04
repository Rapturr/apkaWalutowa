import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CurrencyService {
  private apiUrl = `http://localhost:8080/currencies`;

  constructor(private http: HttpClient){}

  getCurrencyValue(currency: string, name: string): Observable<{value: number}>{
    const body = {currency, name};
    return this.http.post<{value: number}>(`${this.apiUrl}/get-current-currency-value-command`,body);
  }

  getAllRequests(): Observable<any[]>{
    return this.http.get<any[]>(`${this.apiUrl}/requests`);
  }

}
