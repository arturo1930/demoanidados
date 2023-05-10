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

describe('Institucion e2e test', () => {
  const institucionPageUrl = '/institucion';
  const institucionPageUrlPattern = new RegExp('/institucion(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const institucionSample = {};

  let institucion;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/institucions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/institucions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/institucions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (institucion) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/institucions/${institucion.id}`,
      }).then(() => {
        institucion = undefined;
      });
    }
  });

  it('Institucions menu should load Institucions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('institucion');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Institucion').should('exist');
    cy.url().should('match', institucionPageUrlPattern);
  });

  describe('Institucion page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(institucionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Institucion page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/institucion/new$'));
        cy.getEntityCreateUpdateHeading('Institucion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', institucionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/institucions',
          body: institucionSample,
        }).then(({ body }) => {
          institucion = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/institucions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [institucion],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(institucionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Institucion page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('institucion');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', institucionPageUrlPattern);
      });

      it('edit button click should load edit Institucion page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Institucion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', institucionPageUrlPattern);
      });

      it('edit button click should load edit Institucion page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Institucion');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', institucionPageUrlPattern);
      });

      it('last delete button click should delete instance of Institucion', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('institucion').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', institucionPageUrlPattern);

        institucion = undefined;
      });
    });
  });

  describe('new Institucion page', () => {
    beforeEach(() => {
      cy.visit(`${institucionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Institucion');
    });

    it('should create an instance of Institucion', () => {
      cy.get(`[data-cy="valor"]`).type('Algodón').should('have.value', 'Algodón');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        institucion = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', institucionPageUrlPattern);
    });
  });
});
