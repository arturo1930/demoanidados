import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Pantalla e2e test', () => {
  const pantallaPageUrl = '/pantalla';
  const pantallaPageUrlPattern = new RegExp('/pantalla(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const pantallaSample = {};

  let pantalla;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/pantallas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/pantallas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/pantallas/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (pantalla) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/pantallas/${pantalla.id}`,
      }).then(() => {
        pantalla = undefined;
      });
    }
  });

  it('Pantallas menu should load Pantallas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('pantalla');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Pantalla').should('exist');
    cy.url().should('match', pantallaPageUrlPattern);
  });

  describe('Pantalla page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(pantallaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Pantalla page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/pantalla/new$'));
        cy.getEntityCreateUpdateHeading('Pantalla');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', pantallaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/pantallas',
          body: pantallaSample,
        }).then(({ body }) => {
          pantalla = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/pantallas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [pantalla],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(pantallaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Pantalla page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('pantalla');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', pantallaPageUrlPattern);
      });

      it('edit button click should load edit Pantalla page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Pantalla');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', pantallaPageUrlPattern);
      });

      it('edit button click should load edit Pantalla page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Pantalla');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', pantallaPageUrlPattern);
      });

      it('last delete button click should delete instance of Pantalla', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('pantalla').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', pantallaPageUrlPattern);

        pantalla = undefined;
      });
    });
  });

  describe('new Pantalla page', () => {
    beforeEach(() => {
      cy.visit(`${pantallaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Pantalla');
    });

    it('should create an instance of Pantalla', () => {
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        pantalla = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', pantallaPageUrlPattern);
    });
  });
});
