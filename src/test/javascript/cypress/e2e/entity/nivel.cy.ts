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

describe('Nivel e2e test', () => {
  const nivelPageUrl = '/nivel';
  const nivelPageUrlPattern = new RegExp('/nivel(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const nivelSample = {};

  let nivel;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/nivels+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/nivels').as('postEntityRequest');
    cy.intercept('DELETE', '/api/nivels/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (nivel) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/nivels/${nivel.id}`,
      }).then(() => {
        nivel = undefined;
      });
    }
  });

  it('Nivels menu should load Nivels page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('nivel');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Nivel').should('exist');
    cy.url().should('match', nivelPageUrlPattern);
  });

  describe('Nivel page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(nivelPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Nivel page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/nivel/new$'));
        cy.getEntityCreateUpdateHeading('Nivel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', nivelPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/nivels',
          body: nivelSample,
        }).then(({ body }) => {
          nivel = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/nivels+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [nivel],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(nivelPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Nivel page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('nivel');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', nivelPageUrlPattern);
      });

      it('edit button click should load edit Nivel page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Nivel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', nivelPageUrlPattern);
      });

      it('edit button click should load edit Nivel page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Nivel');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', nivelPageUrlPattern);
      });

      it('last delete button click should delete instance of Nivel', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('nivel').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', nivelPageUrlPattern);

        nivel = undefined;
      });
    });
  });

  describe('new Nivel page', () => {
    beforeEach(() => {
      cy.visit(`${nivelPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Nivel');
    });

    it('should create an instance of Nivel', () => {
      cy.get(`[data-cy="valor"]`).type('Sabroso Galicia Hogar').should('have.value', 'Sabroso Galicia Hogar');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        nivel = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', nivelPageUrlPattern);
    });
  });
});
