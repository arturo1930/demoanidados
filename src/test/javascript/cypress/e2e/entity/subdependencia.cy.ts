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

describe('Subdependencia e2e test', () => {
  const subdependenciaPageUrl = '/subdependencia';
  const subdependenciaPageUrlPattern = new RegExp('/subdependencia(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const subdependenciaSample = {};

  let subdependencia;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/subdependencias+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/subdependencias').as('postEntityRequest');
    cy.intercept('DELETE', '/api/subdependencias/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (subdependencia) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/subdependencias/${subdependencia.id}`,
      }).then(() => {
        subdependencia = undefined;
      });
    }
  });

  it('Subdependencias menu should load Subdependencias page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('subdependencia');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Subdependencia').should('exist');
    cy.url().should('match', subdependenciaPageUrlPattern);
  });

  describe('Subdependencia page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(subdependenciaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Subdependencia page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/subdependencia/new$'));
        cy.getEntityCreateUpdateHeading('Subdependencia');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subdependenciaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/subdependencias',
          body: subdependenciaSample,
        }).then(({ body }) => {
          subdependencia = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/subdependencias+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [subdependencia],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(subdependenciaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Subdependencia page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('subdependencia');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subdependenciaPageUrlPattern);
      });

      it('edit button click should load edit Subdependencia page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Subdependencia');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subdependenciaPageUrlPattern);
      });

      it('edit button click should load edit Subdependencia page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Subdependencia');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subdependenciaPageUrlPattern);
      });

      it('last delete button click should delete instance of Subdependencia', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('subdependencia').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subdependenciaPageUrlPattern);

        subdependencia = undefined;
      });
    });
  });

  describe('new Subdependencia page', () => {
    beforeEach(() => {
      cy.visit(`${subdependenciaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Subdependencia');
    });

    it('should create an instance of Subdependencia', () => {
      cy.get(`[data-cy="valor"]`).type('neutral').should('have.value', 'neutral');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        subdependencia = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', subdependenciaPageUrlPattern);
    });
  });
});
