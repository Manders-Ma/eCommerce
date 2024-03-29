import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ProductListComponent } from './components/product-list/product-list.component';
import { HTTP_INTERCEPTORS, HttpClientModule, HttpClientXsrfModule } from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { Routes, RouterModule, mapToCanActivate } from '@angular/router';
import { ProductCategoryMenuComponent } from './components/product-category-menu/product-category-menu.component';
import { SearchComponent } from './components/search/search.component';
import { ProductDetailsComponent } from './components/product-details/product-details.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CartStatusComponent } from './components/cart-status/cart-status.component';
import { CartDetailsComponent } from './components/cart-details/cart-details.component';
import { CheckoutComponent } from './components/checkout/checkout.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginStatusComponent } from './components/login-status/login-status.component';
import { LoginComponent } from './components/login/login.component';
import { AuthInterceptorService } from './interceptors/auth-interceptor.service';
import { authGuard } from './routeguards/auth.guard';
import { LogoutPageComponent } from './components/logout-page/logout-page.component';
import { ProductDetailsModifyComponent } from './components/product-details-modify/product-details-modify.component';
import { roleGuard } from './routeguards/role.guard';
import { ProductCreateComponent } from './components/product-create/product-create.component';
import { OrderHistoryComponent } from './components/order-history/order-history.component';
import { ConfirmPayComponent } from './components/confirm-pay/confirm-pay.component';

// 定義上到下的順序為最具體的路徑到最通用的路徑
const routes: Routes = [
  { path: 'product-details-modify/:id', component: ProductDetailsModifyComponent, canActivate: [authGuard, roleGuard] },
  { path: 'products/:id', component: ProductDetailsComponent },
  { path: 'search/:keyword', component: ProductListComponent },
  { path: 'category/:id', component: ProductListComponent },
  { path: 'confirm-pay', component: ConfirmPayComponent, canActivate: [authGuard] },
  { path: 'order-history', component: OrderHistoryComponent, canActivate: [authGuard] },
  { path: 'product-create', component: ProductCreateComponent, canActivate: [authGuard, roleGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'logout-page', component: LogoutPageComponent, canActivate: [authGuard] },
  { path: 'checkout', component: CheckoutComponent, canActivate: [authGuard] },
  { path: 'cart-details', component: CartDetailsComponent },
  { path: 'category', component: ProductListComponent },
  { path: 'products', component: ProductListComponent },
  { path: '', redirectTo: '/products', pathMatch: 'full' },
  { path: '**', redirectTo: '/products', pathMatch: 'full' }
];

@NgModule({
  declarations: [
    AppComponent,
    ProductListComponent,
    ProductCategoryMenuComponent,
    SearchComponent,
    ProductDetailsComponent,
    CartStatusComponent,
    CartDetailsComponent,
    CheckoutComponent,
    LoginStatusComponent,
    LoginComponent,
    LogoutPageComponent,
    ProductDetailsModifyComponent,
    ProductCreateComponent,
    OrderHistoryComponent,
    ConfirmPayComponent
  ],
  imports: [
    RouterModule.forRoot(routes),
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NgbModule,
    ReactiveFormsModule,
    FormsModule
  ],
  providers: [
    provideAnimationsAsync(),
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
