import {
  User,
  UserManager,
  UserManagerSettings,
  INavigator,
  SignoutRedirectArgs,
} from "oidc-client-ts";

class CognitoUserManager extends UserManager {
  cognito_domain: string;
  post_logout_redirect_uri?: string;
  client_id?: string;

  constructor(
    settings: UserManagerSettings,
    cognitoDomain: string,
    redirectNavigator?: INavigator,
    popupNavigator?: INavigator,
    iframeNavigator?: INavigator
  ) {
    super(settings, redirectNavigator, popupNavigator, iframeNavigator);
    this.cognito_domain = cognitoDomain;
    this.post_logout_redirect_uri = settings.post_logout_redirect_uri;
    this.client_id = settings.client_id;
  }

  async signoutRedirect(args?: SignoutRedirectArgs): Promise<void> {
    super.signoutSilent();
    const endSessionEndpoint = `${this.cognito_domain}/logout`;
    const cb = encodeURIComponent(this.post_logout_redirect_uri);
    const url = `${endSessionEndpoint}?client_id=${this.client_id}&logout_uri=${cb}`;
    window.open(url, "_self");
  }
}

export default class AuthService {
  userManager: UserManager;

  constructor() {
    const settings = {
      authority: process.env.REACT_APP_AUTHORITY!,
      client_id: process.env.REACT_APP_CLIENT_ID!,
      client_secret: process.env.REACT_APP_CLIENT_SECRET!,
      redirect_uri: `${process.env.REACT_APP_CLIENT_BASE_URL}/openid/callback`,
      silent_redirect_uri: `${process.env.REACT_APP_CLIENT_BASE_URL}/openid/callback`,
      post_logout_redirect_uri: `${process.env.REACT_APP_CLIENT_BASE_URL}`,
      response_type: "code",
      scope: process.env.REACT_APP_CLIENT_SCOPE,
    };
    this.userManager =
      process.env.REACT_APP_OIDC_PROVIDER === "cognito"
        ? (this.userManager = new CognitoUserManager(
            settings,
            process.env.REACT_APP_COGNITO_DOMAIN || "null"
          ))
        : new UserManager(settings);
  }

  public getUser(): Promise<User | null> {
    return this.userManager.getUser();
  }

  public login(): Promise<void> {
    return this.userManager.signinRedirect();
  }

  public loginCallback(): Promise<User> {
    return this.userManager.signinRedirectCallback();
  }

  public logout(): Promise<void> {
    return this.userManager.signoutRedirect();
  }
}
