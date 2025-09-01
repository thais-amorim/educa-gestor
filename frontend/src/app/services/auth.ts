import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class Auth {
  
  constructor(private keycloakService: KeycloakService) { }

  public isLoggedIn(): boolean {
    return this.keycloakService.isLoggedIn();
  }

  public getUsername(): string {
    return this.keycloakService.getUsername();
  }

  public getUserRoles(): string[] {
    return this.keycloakService.getUserRoles();
  }

  public login(): void {
    this.keycloakService.login();
  }

  public logout(): void {
    this.keycloakService.logout();
  }

  public getToken(): Promise<string> {
    return this.keycloakService.getToken();
  }

  public getUserProfile(): Promise<any> {
    return this.keycloakService.loadUserProfile();
  }
}
