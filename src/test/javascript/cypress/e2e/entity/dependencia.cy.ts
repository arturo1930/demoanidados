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

describe('Dependencia e2e test', () => {
  const dependenciaPageUrl = '/dependencia';
  const dependenciaPageUrlPattern = new RegExp('/dependencia(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const dependenciaSample = {};

  let dependencia;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/dependencias+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/dependencias').as('postEntityRequest');
    cy.intercept('DELETE', '/api/dependencias/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (dependencia) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/dependencias/${dependencia.id}`,
      }).then(() => {
        dependencia = undefined;
      });
    }
  });

  it('Dependencias menu should load Dependencias page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('dependencia');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Dependencia').should('exist');
    cy.url().should('match', dependenciaPageUrlPattern);
  });

  describe('Dependencia page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(dependenciaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Dependencia page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/dependencia/new$'));
        cy.getEntityCreateUpdateHeading('Dependencia');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dependenciaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/dependencias',
          body: dependenciaSample,
        }).then(({ body }) => {
          dependencia = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/dependencias+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [dependencia],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(dependenciaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Dependencia page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('dependencia');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dependenciaPageUrlPattern);
      });

      it('edit button click should load edit Dependencia page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Dependencia');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dependenciaPageUrlPattern);
      });

      it('edit button click should load edit Dependencia page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Dependencia');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dependenciaPageUrlPattern);
      });

      it('last delete button click should delete instance of Dependencia', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('dependencia').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dependenciaPageUrlPattern);

        dependencia = undefined;
      });
    });
  });

  describe('new Dependencia page', () => {
    beforeEach(() => {
      cy.visit(`${dependenciaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Dependencia');
    });

    it('should create an instance of Dependencia', () => {
      cy.get(`[data-cy="valor"]`).type('Automatizado de').should('have.value', 'Automatizado de');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        dependencia = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', dependenciaPageUrlPattern);
    });
  });
});
