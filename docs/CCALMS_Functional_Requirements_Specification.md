# FUNCTIONAL REQUIREMENTS SPECIFICATION

## CCA License Management System (CCALMS)

| | |
|---|---|
| **Document ID** | FRS-CCALMS-001 |
| **Document Type** | Functional Requirements Specification |
| **Product** | CCALMS (CCA License Management System) |
| **Governing Standard** | ISO/IEC/IEEE 29148:2018 |
| **Classification** | Internal — Engineering / Business |
| **Version** | 2.0 |
| **Status** | Baseline Candidate |
| **Effective Date** | 05 October 2026 |
| **Business Owner** | Controller of Certifying Authorities (CCA), MeitY |
| **Prepared By** | Software Engineering |
| **Reviewed By** | Application Design / Delivery Management |

---

## Document Revision History

| Version | Date | Author | Summary of Change |
|---------|------|--------|-------------------|
| 1.0 | 05-Oct-2026 | Engineering | Initial draft |
| 2.0 | 05-Oct-2026 | Engineering / Delivery | Professional baseline rewrite; tightened scope, interfaces, and shall-statements for design and test use |

## Distribution

| Role | Purpose |
|------|---------|
| Business Owner / CCA | Scope confirmation and acceptance |
| Delivery / Design Lead | Baseline for solution design and work breakdown |
| Development Teams | Implementation contract for functional behaviour |
| QA / Test Management | Traceable test design and acceptance criteria |
| Operations / Release | Deployment and integration readiness |

## How to Read This Document

1. Sections 1–3 establish product context, constraints, and external interfaces. These bind design decisions.
2. Section 4 is the **authoritative functional baseline**. Each requirement uses IEEE 29148 *shall* syntax and is uniquely identified for traceability.
3. Priority values (**Critical / High / Medium / Low**) indicate delivery risk and release gating, not optional intent. Critical items are release blockers unless formally waived.
4. Status values quoted in this specification are **controlled business vocabulary** and must be preserved exactly in implementation and tests.

---

# 1. Introduction

## 1.1 Purpose

This Functional Requirements Specification defines the mandatory functional behaviour of the **CCA License Management System (CCALMS)** — the digital licensing platform operated for the **Controller of Certifying Authorities (CCA), Ministry of Electronics and Information Technology (MeitY)**.

CCALMS supports the statutory licensing lifecycle for **Certifying Authorities (CAs)** under the **Information Technology Act, 2000**. It provides a controlled workflow from intent registration through application, fee verification, scrutiny, audit, in-principle approval, licence issuance, renewal, cessation, ESP (eSign Service Provider) go-live, digital signing, notifications, and management reporting.

This document is the functional contract between business stakeholders and engineering. It specifies **what the system shall do**. It does not prescribe UI visual design, detailed class design, or infrastructure topology except where those choices constrain functional behaviour.

## 1.2 Scope

### 1.2.1 In Scope

The following capabilities constitute the current functional baseline:

| ID | Capability Area |
|----|-----------------|
| S-01 | Intent registration and applicant onboarding |
| S-02 | Authentication, OTP verification, JWT session control, and role lifecycle |
| S-03 | Administrative master data management |
| S-04 | New CA licence application (Individual, Firm/Company, Government Agency) |
| S-05 | Fee-due handling via offline payment-proof upload and CCA verification |
| S-06 | Application scrutiny and formal review decisions |
| S-07 | Pre-licence audit, including agency selection, NC cycle, and annexures |
| S-08 | In-principle approval, supporting document intake, and licence issuance |
| S-09 | Licence renewal |
| S-10 | Licence cessation |
| S-11 | ESP application and go-live approval |
| S-12 | C-DAC eSign integration and signed-document retrieval |
| S-13 | Annual audit for licensed entities |
| S-14 | Multi-channel notifications (email, MSDG DLT SMS, in-application) |
| S-15 | CCA and Licensee dashboards and MIS extracts |
| S-16 | API Gateway routing, authentication enforcement, CORS, and API documentation aggregation |

### 1.2.2 Out of Scope

The following are explicitly excluded from this baseline. Inclusion requires a formal change request and revision of this FRS.

| ID | Exclusion |
|----|-----------|
| OOS-01 | Presentation-layer (frontend) visual design and client-side implementation detail |
| OOS-02 | Online payment gateway / PSP integration; fee collection remains an **offline proof** model |
| OOS-03 | Show-cause and licence suspension business processes (template placeholders exist; no production workflow) |
| OOS-04 | Detailed design of `renew-audit-service` internals (gateway routing is reserved; service delivery is a separate workstream) |
| OOS-05 | Direct hardware interfaces (HSM drivers, scanners, biometric devices) |
| OOS-06 | Asynchronous messaging platforms (Kafka, RabbitMQ, Redis streams) |
| OOS-07 | Statutory drafting or amendment of IT Act / CCA policy instruments |

## 1.3 Product Perspective

CCALMS is a **component of the CCA digital governance estate**, not a standalone desktop product. The solution architecture comprises:

- An **API Gateway** as the controlled entry point (routing, JWT enforcement, CORS)
- Domain **microservices** for authentication, administration, licence acquisition, renewal, audit, issuance, ESP, cessation, annual audit, dashboards, notifications, and eSign
- Optional **service discovery** (Eureka), with static upstream addressing authoritative in current environment profiles
- A shared **PostgreSQL** datastore (`cca_db`)
- Government integrations: **C-DAC eSign**, **MSDG DLT SMS**, and **mGov SMTP**
- A separate **web client** consuming gateway APIs (hosted independently of this backend baseline)

```text
                    Web Client
                         |
                   API Gateway
                         |
     +----------+--------+--------+----------+
     |          |        |        |          |
   Auth      Admin   Licence   Audit    Issuance / ESP / ...
     |          |        |        |          |
     +----------+----+---+--------+----------+
                     |
               PostgreSQL (cca_db)
                     |
        C-DAC eSign | MSDG SMS | mGov SMTP
```

Engineering shall preserve service boundaries and gateway contracts unless a design change is approved through change control.

## 1.4 References

| Ref | Source | Relevance |
|-----|--------|-----------|
| REF-01 | Information Technology Act, 2000 | Legal basis for CA licensing |
| REF-02 | CCA / MeitY operating mandates | Business ownership and process authority |
| REF-03 | ISO/IEC/IEEE 29148:2018 | Requirements engineering structure and *shall* language |
| REF-04 | IEEE Std 830-1998 (superseded) | Historical SRS structure adapted herein |
| REF-05 | CCA-LMS User Guide | End-user operational reference |
| REF-06 | CCALMS SMS Supporting Letter / DLT documentation | SMS template governance |
| REF-07 | C-DAC eSign Level-1 / Level-2 specifications | Digital signature provider contract |
| REF-08 | MSDG DLT SMS interface | Government SMS delivery |
| REF-09 | Environment configuration baselines (`application-*.properties`) | Runtime routing and integration endpoints |
| REF-10 | CCALMS service codebase (`in.lms.cca`) | Implementation reference for behavioural verification |

---

# 2. General Description

## 2.1 Product Functions

CCALMS delivers the following major capability groups:

| Capability | Business Outcome |
|------------|------------------|
| **Identity and Access** | Controlled access with OTP-backed authentication, JWT sessions, and governed role transitions |
| **Master Data Administration** | Consistent reference data for geography, menus, roles, documents, agencies, and configuration masters |
| **Licence Acquisition** | Structured multi-step applications culminating in declaration and fee stage |
| **Fee Verification** | Traceable offline payment-proof intake and CCA verification |
| **Scrutiny and Decisioning** | Formal review outcomes: accept, return for edit, recommend audit, reject, recommend in-principle |
| **Pre-Licence Audit** | Agency selection, audit execution, NC closure, annexures, and signed audit artefacts |
| **Licence Issuance** | In-principle grant, supporting document acceptance, licence artefact issue, and role promotion |
| **Renewal** | Continuity of licensed operations through a controlled renewal path |
| **Cessation** | Orderly exit from licensed status with dual-level CCA approval |
| **ESP Go-Live** | Controlled onboarding of eSign Service Provider capability for licensees |
| **Digital eSign** | Legally significant signing via C-DAC with auditable request/response handling |
| **Annual Audit** | Ongoing compliance oversight for active licensees |
| **Notifications** | Reliable stakeholder communication across email, SMS, and in-application channels |
| **Dashboards and MIS** | Operational visibility and extractable management reports |
| **Platform Controls** | Secure ingress, route discipline, and API discoverability |

## 2.2 User Classes and Characteristics

| User Class | Role Representation | Expertise | Authority Summary |
|------------|---------------------|-----------|-------------------|
| Guest / Public | Unauthenticated | Low | Intent registration; public reference lookups |
| Applicant | `ROLE_APPLICANT` | Low–Medium | Application preparation, document and payment-proof upload, audit agency selection, NC response |
| Licensee | `ROLE_LICENSEE` | Medium | Renewal, annual audit, ESP, cessation, licensee reporting |
| Ex-Licensee | `ROLE_EXLICENSEE` | Low | Post-cessation restricted access |
| Audit Agency / Auditor | `ROLE_AUDIT_AGENCY` | Medium–High | Audit execution, NC management, annexures, audit report submission |
| Application Review Committee | `ROLE_APPLICATION_REVIEW_COMMITTEE` | High | Scrutiny decisions and recommendations |
| CCA Officer | Staff role (`ROLE_<designation>`) | High | Intermediate regulatory approvals (e.g., cessation officer path) |
| CCA | Elevated CCA staff roles | High | Final regulatory decisions across issuance, rejection, ESP, and cessation |
| Administrator | `ADMIN` | High | Master data and configuration control |

Access enforcement is role-based. Services shall reject unauthorised operations irrespective of client presentation.

## 2.3 Design and Implementation Constraints

The following constraints are mandatory for design and delivery. Deviation requires architectural approval.

| ID | Constraint |
|----|------------|
| C-01 | Implementation language: **Java 17** |
| C-02 | Application platform: **Spring Boot 3.3.x** with **Spring Cloud 2023.0.x** (Gateway, Eureka) |
| C-03 | Persistence: **Spring Data JPA** on **PostgreSQL** (`cca_db`), timezone **Asia/Kolkata** |
| C-04 | Security: Spring Security, **JJWT**, **BCrypt** password hashing; gateway JWT filter on protected routes |
| C-05 | Inter-service integration: synchronous HTTP (**RestTemplate**); no message-broker dependency in baseline |
| C-06 | API documentation: **springdoc-openapi** |
| C-07 | Document generation: **iText** / html2pdf; tabular export: **Apache POI** |
| C-08 | Uploaded and generated files: local filesystem storage in baseline |
| C-09 | Digital signature provider: **C-DAC** eSign (Level-1 / Level-2 as configured) |
| C-10 | SMS: **MSDG DLT**, sender identity **CCALMS** |
| C-11 | Email: mGov SMTP (`smtp.mgovcloud.in`), corporate sender `info@cca.gov.in` |
| C-12 | Maven coordinates under group `in.lms.cca` |
| C-13 | OTP validity: **five (5) minutes** unless formally reconfigured |
| C-14 | Environment profiles: local, development, test, UAT, and production |

## 2.4 Assumptions and Dependencies

| ID | Statement |
|----|-----------|
| A-01 | A conforming web client will invoke published gateway contracts. |
| A-02 | PostgreSQL `cca_db` is available with agreed capacity and backup policy. |
| A-03 | C-DAC eSign endpoints and callback path remain reachable; CORS policy includes the C-DAC origin. |
| A-04 | MSDG DLT templates corresponding to `SmsTemplateKey` values remain registered and active. |
| A-05 | mGov SMTP credentials and connectivity remain valid. |
| A-06 | Where Eureka registration is disabled, gateway static URIs are the routing source of truth. |
| A-07 | Renewal-audit routing may be activated when the dedicated service workstream is delivered. |
| A-08 | Applicants complete statutory fee payment outside the system and upload proof for verification. |
| A-09 | Controlled status strings (including historically non-standard spellings such as `RECOMANDED_FOR_AUDIT`) are treated as immutable business keys unless migrated under change control. |

---

# 3. External Interface Requirements

## 3.1 User Interfaces

The presentation client is outside this repository. The rules below bind any conforming UI to backend behaviour.

| ID | Requirement |
|----|-------------|
| UI-01 | Authenticated journeys shall obtain a JWT through credential authentication and OTP verification before invoking protected APIs. |
| UI-02 | Navigation shall be driven by role-resolved menu masters. |
| UI-03 | Licence acquisition shall be presented as a multi-step process with durable per-step persistence and status-aware progression. |
| UI-04 | Status-gated actions (payment proof, agency selection, bank guarantee upload, NC response, review edit) shall be enabled only when the current application status permits the transition. |
| UI-05 | The client shall surface service validation and authorisation failures without masking error semantics required for user correction. |
| UI-06 | Document upload capabilities shall support application packs, payment proofs, annexures, bank guarantees, and undertakings. |
| UI-07 | eSign journeys shall hand off to C-DAC and resume on callback completion to retrieve signed artefacts. |
| UI-08 | Dashboard presentation shall segregate CCA and Licensee views by authorised role. |
| UI-09 | Intent registration shall remain available without authentication. |

## 3.2 Hardware Interfaces

| ID | Statement |
|----|-----------|
| HW-01 | No device-level hardware integration is required in this baseline. |
| HW-02 | Crypto-token or similar attributes, where captured, are **data fields** within forms or annexures only. |

## 3.3 Software Interfaces

| ID | Interface | Characteristics |
|----|-----------|-----------------|
| SW-01 | API Gateway ↔ Domain Services | HTTPS/HTTP; dual path styles `/api/<service>/**` (prefix strip) and `/<service>/**`; JWT on protected routes |
| SW-02 | PostgreSQL `cca_db` | JDBC / JPA persistence for all transactional domains |
| SW-03 | C-DAC eSign | Sign request initiation; callback to `/esign-service/esignresponse` |
| SW-04 | MSDG DLT SMS | Templated SMS; OTP mode for authentication OTPs; single-message mode for business templates |
| SW-05 | mGov SMTP | Operational and transactional email delivery |
| SW-06 | Web Client Origins | CORS allow-list including production host `https://ccalms.cca.gov.in` and approved environment hosts |
| SW-07 | Eureka (optional) | Service discovery when enabled |
| SW-08 | Inter-service HTTP clients | Synchronous orchestration between domain services |
| SW-09 | Filesystem document store | Persistence of uploads and generated PDFs |

### Service Portfolio (Logical)

| Service | Port | Gateway Path |
|---------|------|--------------|
| eureka-server | 7777 | — |
| api-gateway | 80 | `/` |
| auth-service | 7779 | `/auth-service/**` |
| notification-service | 7780 | `/notification-service/**` |
| esign-service | 7781 | `/esign-service/**` |
| admin-service | 7782 | `/admin-service/**` |
| newlicense-service | 7783 | `/newlicense-service/**` |
| license-issuance-service | 7784 | `/license-issuance-service/**` |
| esign-application-service | 7785 | `/esign-application-service/**` |
| renewal-service | 7786 | `/renew-license-service/**` |
| audit-service | 7787 | `/audit-service/**` |
| renew-audit-service | 7788 | `/renew-audit-service/**` |
| cessation-service | 7789 | `/cessation-service/**` |
| dashboard-service | 7790 | `/dashboard-service/**` |
| annual-audit-service | 7791 | `/annual-audit-service/**` |

---

# 4. Specific Functional Requirements

Requirements are organised by module. Each module follows the IEEE 29148 pattern:

1. Description and Priority  
2. Stimulus / Response Sequences  
3. Functional Requirements Statements (*shall* constraints)

**Statement template:**  
`[Condition] + [Subject] + shall + [Action] + [Object] + [Constraint]`

---

## 4.1 Identity and Access Management

### 4.1.1 Description and Priority

Establishes trusted identity for all authenticated operations: login provisioning, OTP challenge, JWT issuance and refresh, password recovery, and governed role transitions across Applicant, Licensee, and Ex-Licensee states.

**Priority:** Critical  
**Primary services:** auth-service, api-gateway

### 4.1.2 Stimulus / Response Sequences

| Stimulus | Expected System Response |
|----------|--------------------------|
| Valid credentials submitted | Prior OTPs inactivated; fresh email and mobile OTPs generated and dispatched |
| Valid OTP submitted | Access token and refresh token issued |
| Invalid or expired OTP submitted | Verification rejected; no tokens issued |
| Valid refresh token presented | New access token issued |
| Logout requested | Session / refresh credentials invalidated per policy |
| Intent login provisioning | Applicant account created with `ROLE_APPLICANT` |
| Licence issuance completed | Role transition Applicant → Licensee |
| Cessation approved | Role transition Licensee → Ex-Licensee |
| Protected route without valid JWT | Gateway denies request prior to service invocation |

### 4.1.3 Functional Requirements Statements

| Requirement ID | Functional Statement | Inputs and Validation |
|----------------|----------------------|-----------------------|
| REQ-AUTH-001 | When valid credentials are submitted [Condition], the system [Subject] shall generate [Action] distinct email and mobile OTPs [Object] and inactivate all previously active OTPs for that username [Constraint]. | Username; password verified via BCrypt; OTP lifetime 5 minutes |
| REQ-AUTH-002 | When OTPs are generated [Condition], the system [Subject] shall dispatch [Action] the email OTP by SMTP and the mobile OTP by MSDG DLT using template key `otp` [Object] without delay [Constraint]. | `notificationType`: Email & SMS; `smsTemplateKey`: `otp` |
| REQ-AUTH-003 | When an active, unexpired OTP is verified [Condition], the system [Subject] shall issue [Action] a JWT access token and refresh token [Object] for the authenticated principal [Constraint]. | Username; OTP value; expiry check |
| REQ-AUTH-004 | If the OTP is inactive or expired [Condition], the system [Subject] shall reject [Action] verification [Object] and shall not issue tokens [Constraint]. | Deterministic auth failure response |
| REQ-AUTH-005 | When a valid refresh token is presented [Condition], the system [Subject] shall issue [Action] a new access token [Object] without requiring password re-entry [Constraint]. | Non-empty refresh token |
| REQ-AUTH-006 | When a password-reset OTP is requested for a registered username [Condition], the system [Subject] shall deliver [Action] reset OTPs [Object] through the configured notification channels [Constraint]. | Username must exist |
| REQ-AUTH-007 | When an authenticated user submits a valid password-change request [Condition], the system [Subject] shall replace [Action] the stored password hash [Object] using BCrypt [Constraint]. | Current password; new password meeting policy |
| REQ-AUTH-008 | When CCA staff provisioning is requested with an allowable role [Condition], the system [Subject] shall create [Action] a staff login with role `ROLE_<name>` [Object] and persist the staff profile [Constraint]. | Role validation against RoleMaster / reserved-name rules |
| REQ-AUTH-009 | Upon successful licence issuance [Condition], the system [Subject] shall transition [Action] the user role from `ROLE_APPLICANT` to `ROLE_LICENSEE` [Object] through the designated auth API [Constraint]. | Username bound to issued licence |
| REQ-AUTH-010 | Upon approved cessation [Condition], the system [Subject] shall transition [Action] the user role from `ROLE_LICENSEE` to `ROLE_EXLICENSEE` [Object] through the designated auth API [Constraint]. | Username bound to cessation record |
| REQ-AUTH-011 | When a protected gateway route is invoked without a valid JWT [Condition], the API Gateway [Subject] shall deny [Action] the request [Object] before forwarding [Constraint]. | AuthenticationFilter on protected routes |

---

## 4.2 Intent Registration and Onboarding

### 4.2.1 Description and Priority

Captures prospective CA intent, allocates a unique intent identifier, and provisions applicant credentials to commence the licensing journey.

**Priority:** Critical  
**Primary services:** admin-service, auth-service, notification-service

### 4.2.2 Stimulus / Response Sequences

| Stimulus | Expected System Response |
|----------|--------------------------|
| Guest submits intent registration | Intent persisted; unique code allocated; applicant login created; confirmation notified |
| Public geo / intent reference requested | Reference data returned without authentication |

### 4.2.3 Functional Requirements Statements

| Requirement ID | Functional Statement | Inputs and Validation |
|----------------|----------------------|-----------------------|
| REQ-INT-001 | When a guest submits a complete intent registration [Condition], the system [Subject] shall persist [Action] an Intent record with a unique intent code [Object] and provision an associated applicant login [Constraint]. | Mandatory identity and contact fields; unique intent code |
| REQ-INT-002 | Upon successful intent persistence [Condition], the system [Subject] shall notify [Action] the applicant [Object] using template key `intent-registration` and email [Constraint]. | Channel: Email & SMS |
| REQ-INT-003 | When intent registration is invoked on a designated public route [Condition], the system [Subject] shall accept [Action] the request [Object] without JWT authentication [Constraint]. | Gateway open-route policy |

---

## 4.3 Master Data and Configuration

### 4.3.1 Description and Priority

Provides governed maintenance of reference data consumed by forms, menus, validation, and workflow selection (geography, roles, documents, agencies, CPS, undertakings, eKYC/eSign masters).

**Priority:** High  
**Primary service:** admin-service

### 4.3.2 Stimulus / Response Sequences

| Stimulus | Expected System Response |
|----------|--------------------------|
| Administrator maintains geo hierarchy | Country / State / City records upserted with status |
| Administrator maintains menus, roles, services, documents | Masters persisted for client and validation use |
| Administrator maintains audit agencies and auditors | Selectable agency catalogue updated; login linkage supported |
| Non-administrator attempts write | Operation rejected |

### 4.3.3 Functional Requirements Statements

| Requirement ID | Functional Statement | Inputs and Validation |
|----------------|----------------------|-----------------------|
| REQ-ADM-001 | When an authorised Administrator submits geo master data [Condition], the system [Subject] shall create or update [Action] Country, State, and City records [Object] preserving hierarchical integrity [Constraint]. | State→Country; City→State |
| REQ-ADM-002 | When RoleMaster maintenance is performed [Condition], the system [Subject] shall enforce [Action] validated role display names [Object] including APPLICANT, ADMIN, AUDIT_AGENCY, and LICENSEE as applicable [Constraint]. | Controller validation rules |
| REQ-ADM-003 | When an audit agency is registered [Condition], the system [Subject] shall persist [Action] agency and auditor details [Object] and support audit-agency login provisioning [Constraint]. | Active agency profile required for selection use |
| REQ-ADM-004 | When document, service, menu, CPS, undertaking, eKYC, or eSign masters are maintained [Condition], the system [Subject] shall store [Action] the configuration [Object] for downstream validation and navigation [Constraint]. | Master-specific mandatory attributes |
| REQ-ADM-005 | When a non-Administrator attempts master write operations [Condition], the system [Subject] shall reject [Action] the request [Object] as unauthorised [Constraint]. | JWT role enforcement |

---

## 4.4 New Licence Acquisition

### 4.4.1 Description and Priority

Enables Applicants to complete type-specific multi-step applications (Individual, Firm/Company, Government Agency), submit declarations, generate application PDFs, upload supporting documents, and enter the fee-verification stage.

**Priority:** Critical  
**Primary service:** newlicense-service

### 4.4.2 Stimulus / Response Sequences

| Stimulus | Expected System Response |
|----------|--------------------------|
| Applicant saves a form step | Step data persisted against the intent application |
| Applicant submits declaration | Status set to `Submitted`; notification dispatched |
| Application enters fee stage | Status set to `Fee Payment` |
| Applicant uploads payment proof | Status set to `PaymentProof Recieved` |
| PDF generation requested | Application PDF produced |

### 4.4.3 Functional Requirements Statements

| Requirement ID | Functional Statement | Inputs and Validation |
|----------------|----------------------|-----------------------|
| REQ-NLIC-001 | When an Individual Applicant saves an application step [Condition], the system [Subject] shall persist [Action] the step payload [Object] against the IntentApplication for the authenticated user [Constraint]. | JWT-bound username; step APIs |
| REQ-NLIC-002 | When a Firm/Company or Government Agency Applicant saves application steps [Condition], the system [Subject] shall persist [Action] the corresponding firm or government entities [Object] using type-specific endpoints [Constraint]. | Application-type discriminant |
| REQ-NLIC-003 | When the declaration is submitted [Condition], the system [Subject] shall set [Action] status to `Submitted` [Object] and notify using template `license-application` [Constraint]. | Declaration acceptance required |
| REQ-NLIC-004 | When the application enters the fee stage [Condition], the system [Subject] shall set [Action] status to `Fee Payment` [Object] and enable payment-proof upload [Constraint]. | Status gate |
| REQ-NLIC-005 | When a valid payment proof is uploaded while status is `Fee Payment` [Condition], the system [Subject] shall store [Action] the PaymentProof [Object] and update status to `PaymentProof Recieved` [Constraint]. | File mandatory |
| REQ-NLIC-006 | When application PDF generation is requested [Condition], the system [Subject] shall generate [Action] a PDF artefact [Object] using the approved document engine [Constraint]. | Authorised requester |
| REQ-NLIC-007 | Throughout acquisition [Condition], the system [Subject] shall maintain [Action] status values from the controlled vocabulary [Object] defined for the licensing lifecycle [Constraint]. | Includes: pending, Submitted, Fee Payment, PaymentProof Recieved, underReview, Edit Upon Review, Approved From Review, RECOMANDED_FOR_AUDIT, Agency Selection, RECOMMENDED_IN_PRINCIPLE_APPROVAL, IN_PRINCIPLE_APPROVAL_GRANTED, BANK_GUARANTEE_PROOF_UPLOADED / DOCUMENT_SUBMITTED_FOR_LICENSE, LICENSE_ISSUED, and defined rejection/NC states |

---

## 4.5 Application Scrutiny and Review

### 4.5.1 Description and Priority

Provides formal decisioning after payment verification: return for edit, acceptance, recommendation for audit, recommendation for rejection, and recommendation for in-principle approval.

**Priority:** Critical  
**Primary service:** newlicense-service (review controllers); supporting notifications

### 4.5.2 Stimulus / Response Sequences

| Stimulus | Expected System Response |
|----------|--------------------------|
| CCA completes payment verification | Application becomes eligible for scrutiny |
| Reviewer returns for edit | Status `Edit Upon Review`; applicant notified |
| Reviewer accepts | Status `Approved From Review`; applicant notified |
| Reviewer recommends audit | Status `RECOMANDED_FOR_AUDIT`; notification dispatched |
| Rejection confirmed | Rejection status applied; rejection notification dispatched |
| In-principle recommended | Status `RECOMMENDED_IN_PRINCIPLE_APPROVAL` |

### 4.5.3 Functional Requirements Statements

| Requirement ID | Functional Statement | Inputs and Validation |
|----------------|----------------------|-----------------------|
| REQ-REV-001 | When CCA completes payment verification for a proof-received application [Condition], the system [Subject] shall mark [Action] the application eligible for review [Object] against PaymentVerification records [Constraint]. | Linked PaymentProof |
| REQ-REV-002 | When a Review Committee returns an application for edit [Condition], the system [Subject] shall set [Action] Edit Upon Review status [Object] and notify using `application-review` [Constraint]. | Remarks required |
| REQ-REV-003 | When a Review Committee accepts an application [Condition], the system [Subject] shall set [Action] status to `Approved From Review` [Object] and notify using `application-acceptance` [Constraint]. | Authorised review role |
| REQ-REV-004 | When audit is recommended [Condition], the system [Subject] shall set [Action] status to `RECOMANDED_FOR_AUDIT` [Object] and notify using `recommendation-for-audit` [Constraint]. | Controlled status key |
| REQ-REV-005 | When rejection is confirmed [Condition], the system [Subject] shall apply [Action] the rejection status path [Object] and notify using `application-rejection` [Constraint]. | Rejection remarks retained |
| REQ-REV-006 | When an Applicant resubmits after return-for-edit [Condition], the system [Subject] shall accept [Action] updates [Object] only while status is Edit Upon Review [Constraint]. | Strict status gate |
| REQ-REV-007 | When in-principle approval is recommended [Condition], the system [Subject] shall set [Action] status to `RECOMMENDED_IN_PRINCIPLE_APPROVAL` [Object] pending CCA grant [Constraint]. | Exact status match |

---

## 4.6 Pre-Licence Audit

### 4.6.1 Description and Priority

Executes the pre-licence assurance cycle: agency selection, audit submission, NC raise and closure, annexure capture, committee/CCA review, and digital signing of final audit artefacts.

**Priority:** Critical  
**Primary services:** audit-service, esign-service

### 4.6.2 Stimulus / Response Sequences

| Stimulus | Expected System Response |
|----------|--------------------------|
| Applicant selects audit agency | Selection persisted; stakeholders notified |
| CCA approves or rejects agency | Selection decision recorded |
| Auditor submits findings / NC | Audit and NC records persisted; status updated |
| Applicant submits NC corrective action | Status advanced for re-review |
| Committee / CCA completes review | Decision recorded; notifications dispatched |
| Final report signing requested | eSign transaction initiated |

### 4.6.3 Functional Requirements Statements

| Requirement ID | Functional Statement | Inputs and Validation |
|----------------|----------------------|-----------------------|
| REQ-AUD-001 | When an application is recommended for audit [Condition], the system [Subject] shall permit [Action] Applicant selection of an active audit agency and auditors [Object] from administered masters [Constraint]. | Agency must be active |
| REQ-AUD-002 | Upon agency selection submission [Condition], the system [Subject] shall notify [Action] relevant parties [Object] using template `audit-agency-selection` [Constraint]. | Template key mandatory |
| REQ-AUD-003 | When an Auditor submits audit findings [Condition], the system [Subject] shall persist [Action] audit results against criteria/controls [Object] and advance workflow status [Constraint]. | Application identifier required |
| REQ-AUD-004 | When an Auditor raises an NC [Condition], the system [Subject] shall set [Action] status to `NC Issued By Auditor` [Object] and require Applicant corrective action [Constraint]. | NC content and linkage |
| REQ-AUD-005 | When an Applicant submits NC corrective action [Condition], the system [Subject] shall set [Action] status to `NC Action taken By Applicant` [Object] and route for auditor/committee review [Constraint]. | Supporting evidence as required |
| REQ-AUD-006 | When committee or CCA completes audit review [Condition], the system [Subject] shall record [Action] the decision [Object] and notify using `audit-report-review` or `application-rejection` as applicable [Constraint]. | Decision and remarks |
| REQ-AUD-007 | When annexure datasets are submitted [Condition], the system [Subject] shall persist [Action] annexure entities [Object] against the audit application [Constraint]. | Annexure-specific schemas |
| REQ-AUD-008 | When final audit report signing is requested [Condition], the system [Subject] shall initiate [Action] a C-DAC eSign transaction [Object] and retain the signed artefact reference on successful callback [Constraint]. | Depends on Section 4.11 |

---

## 4.7 Licence Issuance and Lifecycle

### 4.7.1 Description and Priority

Completes regulatory grant: in-principle approval, intake of bank guarantee / supporting documents, licence artefact issuance, role promotion to Licensee, licence status administration, and expiry-oriented scheduling.

**Priority:** Critical  
**Primary services:** license-issuance-service, auth-service

### 4.7.2 Stimulus / Response Sequences

| Stimulus | Expected System Response |
|----------|--------------------------|
| CCA grants in-principle approval | Status `IN_PRINCIPLE_APPROVAL_GRANTED`; notification sent |
| Applicant uploads required proofs | Status advanced to BG uploaded / documents submitted |
| CCA issues licence | Licence persisted; status `LICENSE_ISSUED`; role promoted; issuance notified |
| Scheduler identifies nearing expiry | Renewal reminders triggered |

### 4.7.3 Functional Requirements Statements

| Requirement ID | Functional Statement | Inputs and Validation |
|----------------|----------------------|-----------------------|
| REQ-ISS-001 | When CCA grants in-principle approval for a recommended application [Condition], the system [Subject] shall set [Action] status to `IN_PRINCIPLE_APPROVAL_GRANTED` [Object] and notify the applicant [Constraint]. | Prior status must be recommended in-principle |
| REQ-ISS-002 | When required post-approval documents are uploaded [Condition], the system [Subject] shall store [Action] the artefacts [Object] and update status to the document-submitted / bank-guarantee-uploaded state [Constraint]. | File mandatory |
| REQ-ISS-003 | When CCA issues a licence [Condition], the system [Subject] shall persist [Action] LicenceDetails [Object], set status to `LICENSE_ISSUED`, produce licence artefacts, promote the user to Licensee, and notify using `license-issuance` [Constraint]. | Atomic business completion criteria for issuance |
| REQ-ISS-004 | When licence details are requested by username [Condition], the system [Subject] shall return [Action] the licence record [Object] to authorised callers [Constraint]. | Authorisation check |
| REQ-ISS-005 | When an authorised CCA user changes licence status [Condition], the system [Subject] shall update [Action] the licence status [Object] with an auditable change [Constraint]. | Authorised role only |
| REQ-ISS-006 | When the licence scheduler executes [Condition], the system [Subject] shall identify [Action] licences approaching expiry [Object] and dispatch renewal reminders per schedule configuration [Constraint]. | Scheduler configuration |

---

## 4.8 Licence Renewal

### 4.8.1 Description and Priority

Provides Licensees a controlled renewal path, functionally aligned to acquisition, under the renewal service boundary, with renewal-specific status and notifications.

**Priority:** High  
**Primary services:** renewal-service, license-issuance-service

### 4.8.2 Stimulus / Response Sequences

| Stimulus | Expected System Response |
|----------|--------------------------|
| Licensee submits renewal | Status `SUBMITTED-FOR-RENEWAL`; renewal notification sent |
| Review / audit / issuance proceeds | Decision chain executed under renewal endpoints |
| Renewal culminating in re-issue | Licence validity updated; renewal notifications sent |

### 4.8.3 Functional Requirements Statements

| Requirement ID | Functional Statement | Inputs and Validation |
|----------------|----------------------|-----------------------|
| REQ-REN-001 | When a Licensee submits a renewal application [Condition], the system [Subject] shall persist [Action] renewal data [Object] and set status to `SUBMITTED-FOR-RENEWAL` [Constraint]. | Role: `ROLE_LICENSEE`; template `renewal-application` |
| REQ-REN-002 | When renewal review decisions are recorded [Condition], the system [Subject] shall apply [Action] status transitions [Object] under `/renew-license-service` analogous to new-licence scrutiny [Constraint]. | Renewal service boundary |
| REQ-REN-003 | When CCA completes renewal culminating in re-issue [Condition], the system [Subject] shall update [Action] licence validity [Object] and notify using `renewal-approval` and/or `license-renewal` [Constraint]. | Updated validity period retained |

---

## 4.9 Licence Cessation

### 4.9.1 Description and Priority

Supports orderly cessation of licensed status through Licensee application, CCA Officer decision, and CCA final decision, with role transition to Ex-Licensee on approval.

**Priority:** High  
**Primary services:** cessation-service, auth-service

### 4.9.2 Stimulus / Response Sequences

| Stimulus | Expected System Response |
|----------|--------------------------|
| Licensee submits cessation application | Application persisted; `cessation-application` notified |
| CCA Officer approves or rejects | Officer decision recorded |
| CCA approves | Cessation finalised; role → Ex-Licensee; approval notified |
| CCA rejects | Licensed status retained; rejection recorded |

### 4.9.3 Functional Requirements Statements

| Requirement ID | Functional Statement | Inputs and Validation |
|----------------|----------------------|-----------------------|
| REQ-CES-001 | When a Licensee submits a cessation application with required undertakings [Condition], the system [Subject] shall persist [Action] the cessation application [Object] and notify using `cessation-application` [Constraint]. | Undertakings / documents as mandated |
| REQ-CES-002 | When a CCA Officer acts on a pending cessation application [Condition], the system [Subject] shall record [Action] the officer approve or reject decision [Object] before CCA final action [Constraint]. | Ordered approval chain |
| REQ-CES-003 | When CCA approves cessation [Condition], the system [Subject] shall finalise [Action] cessation [Object], notify `cessation-approval`, and transition role to `ROLE_EXLICENSEE` [Constraint]. | Role transition mandatory on approval |
| REQ-CES-004 | When CCA rejects cessation [Condition], the system [Subject] shall retain [Action] active licensee status [Object] and record rejection remarks [Constraint]. | Remarks retained for audit |

---

## 4.10 ESP (eSign Service Provider) Application

### 4.10.1 Description and Priority

Enables Licensees to apply for ESP go-live through a multi-step application, formal review, recommendation, and CCA approval.

**Priority:** High  
**Primary service:** esign-application-service

### 4.10.2 Stimulus / Response Sequences

| Stimulus | Expected System Response |
|----------|--------------------------|
| Licensee saves ESP steps | Progressive application data persisted |
| Licensee submits for review | Application locked for review; review notification sent |
| CCA recommends go-live | Recommendation recorded |
| CCA approves go-live | Go-live approved; notification sent |

### 4.10.3 Functional Requirements Statements

| Requirement ID | Functional Statement | Inputs and Validation |
|----------------|----------------------|-----------------------|
| REQ-ESP-001 | When a Licensee saves ESP step one, two, or three [Condition], the system [Subject] shall persist [Action] the step payload including eKYC modes and purpose data [Object] against the ESP application [Constraint]. | Step APIs; role Licensee |
| REQ-ESP-002 | When the ESP application is submitted for review [Condition], the system [Subject] shall mark [Action] the application submitted [Object] and notify using `esp-application-review` [Constraint]. | Submission completeness checks |
| REQ-ESP-003 | When CCA recommends ESP go-live [Condition], the system [Subject] shall record [Action] the recommendation [Object] pending final approval [Constraint]. | Authorised CCA actor |
| REQ-ESP-004 | When CCA approves ESP go-live [Condition], the system [Subject] shall mark [Action] go-live approved [Object] and notify using `esp-go-live` [Constraint]. | Final approval authority |
| REQ-ESP-005 | When ESP applications expire or are rejected [Condition], the system [Subject] shall expose [Action] authorised list views [Object] of expired and rejected applications [Constraint]. | Role-restricted access |

---

## 4.11 Digital Signing (eSign)

### 4.11.1 Description and Priority

Integrates C-DAC eSign to produce digitally signed artefacts for applications, audit reports, and related documents, with auditable request, callback, and retrieval.

**Priority:** Critical  
**Primary services:** esign-service, api-gateway

### 4.11.2 Stimulus / Response Sequences

| Stimulus | Expected System Response |
|----------|--------------------------|
| Sign initiation requested | ESign request created; provider hand-off payload returned |
| C-DAC posts callback | Response persisted; signed artefact linked |
| Signed document requested | Signed PDF returned to authorised requester |

### 4.11.3 Functional Requirements Statements

| Requirement ID | Functional Statement | Inputs and Validation |
|----------------|----------------------|-----------------------|
| REQ-ESIGN-001 | When an authorised user initiates eSign for a supported document [Condition], the system [Subject] shall create [Action] an ESignRequest [Object] for the configured C-DAC level [Constraint]. | Document reference mandatory |
| REQ-ESIGN-002 | When C-DAC posts the eSign callback [Condition], the platform [Subject] shall accept [Action] the cross-origin callback [Object] from the approved C-DAC origin and process `/esignresponse` [Constraint]. | CORS allow-list includes C-DAC |
| REQ-ESIGN-003 | Upon successful callback processing [Condition], the system [Subject] shall persist [Action] ESignResponse and signed-document metadata [Object] for retrieval [Constraint]. | Durable linkage to source document |
| REQ-ESIGN-004 | When a digitally signed document is requested [Condition], the system [Subject] shall return [Action] the signed PDF [Object] to the authorised requester [Constraint]. | Authorisation enforced |

---

## 4.12 Annual Audit

### 4.12.1 Description and Priority

Supports ongoing compliance through annual audit scheduling, agency selection, execution/review, and CCA decision states for Licensees.

**Priority:** High  
**Primary services:** annual-audit-service, dashboard-service

### 4.12.2 Stimulus / Response Sequences

| Stimulus | Expected System Response |
|----------|--------------------------|
| Annual cycle becomes due | Annual audit actions enabled for Licensee |
| Agency selected / audit executed | Workflow advances with notifications as configured |
| CCA reviews submission | State recorded as APPROVED, REJECTED, or PENDING |
| Dashboard requests SLA metrics | On-time and late datasets returned with exports |

### 4.12.3 Functional Requirements Statements

| Requirement ID | Functional Statement | Inputs and Validation |
|----------------|----------------------|-----------------------|
| REQ-AAUD-001 | When an annual audit cycle is due [Condition], the system [Subject] shall enable [Action] annual audit workflow actions [Object] for the Licensee under annual-audit-service [Constraint]. | Role: `ROLE_LICENSEE` |
| REQ-AAUD-002 | When a Licensee selects an audit agency for annual audit [Condition], the system [Subject] shall persist [Action] the selection [Object] and notify using `audit-agency-selection` where configured [Constraint]. | Active agency |
| REQ-AAUD-003 | When CCA reviews an annual audit submission [Condition], the system [Subject] shall record [Action] APPROVED, REJECTED, or PENDING [Object] per workflow rules [Constraint]. | Unset default treated as NOT_SUBMITTED |
| REQ-AAUD-004 | When dashboard services request annual audit SLA metrics [Condition], the system [Subject] shall return [Action] on-time and late datasets [Object] with downloadable exports [Constraint]. | Authorised dashboard access |

---

## 4.13 Notifications

### 4.13.1 Description and Priority

Provides a single notification capability for email, MSDG DLT SMS, and in-application messages, driven by governed template keys for lifecycle events.

**Priority:** Critical  
**Primary service:** notification-service

### 4.13.2 Stimulus / Response Sequences

| Stimulus | Expected System Response |
|----------|--------------------------|
| Upstream service submits notification request | Notification persisted; channels dispatched per type |
| OTP template key used | SMS sent via MSDG OTP mode |
| Business template key used | SMS sent via matching DLT template |
| User queries notifications | Notifications returned by username and role |

### 4.13.3 Functional Requirements Statements

| Requirement ID | Functional Statement | Inputs and Validation |
|----------------|----------------------|-----------------------|
| REQ-NOT-001 | When a service submits a notification request [Condition], notification-service [Subject] shall persist [Action] the notification [Object] and dispatch channels according to `notificationType` [Constraint]. | Username, subject, message, type |
| REQ-NOT-002 | When `smsTemplateKey` equals `otp` [Condition], the system [Subject] shall send [Action] SMS using MSDG OTP mode [Object] with the configured OTP template [Constraint]. | OTP variable binding |
| REQ-NOT-003 | When a non-OTP business template key is supplied [Condition], the system [Subject] shall send [Action] SMS using the registered DLT template [Object] for that key [Constraint]. | Keys include intent-registration, license-application, application-review, application-acceptance, recommendation-for-audit, audit-agency-selection, audit-report-review, application-approval, license-issuance, esp-application-review, esp-go-live, application-rejection, cessation-application, cessation-approval, renewal-application, renewal-approval, license-renewal |
| REQ-NOT-004 | When notifications are requested by username and role [Condition], the system [Subject] shall return [Action] the notification list [Object] ordered by recency [Constraint]. | Authenticated access |
| REQ-NOT-005 | Template keys `show-cause` and `license-suspension` [Condition] are reserved [Subject] and shall not activate [Action] production workflows [Object] until corresponding processes are baselined in this FRS [Constraint]. | Aligns with OOS-03 |

---

## 4.14 Dashboards and MIS

### 4.14.1 Description and Priority

Delivers operational dashboards and management information for CCA and Licensee stakeholders, including DSC/eSign statistics, licensing views, annual audit KPIs, and exportable reports.

**Priority:** Medium  
**Primary service:** dashboard-service

### 4.14.2 Stimulus / Response Sequences

| Stimulus | Expected System Response |
|----------|--------------------------|
| CCA requests dashboard | Aggregated CCA indicators returned |
| Licensee requests dashboard | Licensee-scoped indicators returned |
| DSC/eSign report requested | Statistical dataset and optional download returned |
| Annual audit KPI download requested | Excel/PDF artefact generated |

### 4.14.3 Functional Requirements Statements

| Requirement ID | Functional Statement | Inputs and Validation |
|----------------|----------------------|-----------------------|
| REQ-DASH-001 | When an authorised CCA user requests the CCA dashboard [Condition], the system [Subject] shall return [Action] aggregated licensing, audit, and ESP indicators [Object] for the CCA view [Constraint]. | CCA role required |
| REQ-DASH-002 | When an authorised Licensee requests the licensee dashboard [Condition], the system [Subject] shall return [Action] indicators scoped to that licensee [Object] only [Constraint]. | Tenant/user isolation |
| REQ-DASH-003 | When DSC/eSign report APIs are invoked [Condition], the system [Subject] shall return [Action] statistical datasets [Object] for the requested period, including downloads where offered [Constraint]. | Period filters supported |
| REQ-DASH-004 | When annual audit KPI download endpoints are invoked [Condition], the system [Subject] shall generate [Action] export artefacts [Object] for on-time and late submissions [Constraint]. | Authorised access |

---

## 4.15 Platform and API Gateway Controls

### 4.15.1 Description and Priority

Defines ingress discipline: dual route styles, JWT enforcement, CORS policy, public-route exceptions, and API documentation aggregation.

**Priority:** Critical  
**Primary service:** api-gateway

### 4.15.2 Stimulus / Response Sequences

| Stimulus | Expected System Response |
|----------|--------------------------|
| Request to `/api/<service>/**` | `/api` prefix stripped; forwarded to upstream |
| Request to `/<service>/**` | Forwarded per direct route configuration |
| Protected route without JWT | Request denied |
| Public auth / intent / geo / eSign callback | Allowed per open-route policy |
| Browser call from allow-listed origin | CORS permitted with credentials |

### 4.15.3 Functional Requirements Statements

| Requirement ID | Functional Statement | Inputs and Validation |
|----------------|----------------------|-----------------------|
| REQ-PLT-001 | When a request matches `/api/<service>/**` [Condition], the API Gateway [Subject] shall strip [Action] the `/api` prefix [Object] and route to the configured upstream [Constraint]. | StripPrefix enabled |
| REQ-PLT-002 | When a request matches `/<service>/**` [Condition], the API Gateway [Subject] shall route [Action] to the corresponding upstream service [Object] using the direct route definition [Constraint]. | Dual-route parity of destination |
| REQ-PLT-003 | For routes configured with AuthenticationFilter [Condition], the API Gateway [Subject] shall validate [Action] the JWT [Object] before proxying [Constraint]. | Exceptions only for approved open routes |
| REQ-PLT-004 | When the request Origin is allow-listed [Condition], the gateway [Subject] shall permit [Action] CORS with credentials [Object] for GET, POST, PUT, DELETE, and OPTIONS [Constraint]. | Includes production CCALMS host and approved callbacks |
| REQ-PLT-005 | When API documentation aggregation is enabled [Condition], the platform [Subject] shall expose [Action] consolidated OpenAPI documentation [Object] for service discovery by engineering and test teams [Constraint]. | springdoc-openapi |

---

# 5. Requirements Traceability Matrix

| Module | Requirement IDs | Primary Services | Priority Band |
|--------|-----------------|------------------|---------------|
| Identity and Access | REQ-AUTH-001 – REQ-AUTH-011 | auth-service, api-gateway | Critical |
| Intent and Onboarding | REQ-INT-001 – REQ-INT-003 | admin-service, auth-service, notification-service | Critical |
| Master Data | REQ-ADM-001 – REQ-ADM-005 | admin-service | High |
| New Licence Acquisition | REQ-NLIC-001 – REQ-NLIC-007 | newlicense-service | Critical |
| Scrutiny and Review | REQ-REV-001 – REQ-REV-007 | newlicense-service | Critical |
| Pre-Licence Audit | REQ-AUD-001 – REQ-AUD-008 | audit-service, esign-service | Critical |
| Licence Issuance | REQ-ISS-001 – REQ-ISS-006 | license-issuance-service, auth-service | Critical |
| Renewal | REQ-REN-001 – REQ-REN-003 | renewal-service, license-issuance-service | High |
| Cessation | REQ-CES-001 – REQ-CES-004 | cessation-service, auth-service | High |
| ESP Go-Live | REQ-ESP-001 – REQ-ESP-005 | esign-application-service | High |
| Digital eSign | REQ-ESIGN-001 – REQ-ESIGN-004 | esign-service, api-gateway | Critical |
| Annual Audit | REQ-AAUD-001 – REQ-AAUD-004 | annual-audit-service, dashboard-service | High |
| Notifications | REQ-NOT-001 – REQ-NOT-005 | notification-service | Critical |
| Dashboards and MIS | REQ-DASH-001 – REQ-DASH-004 | dashboard-service | Medium |
| Platform Controls | REQ-PLT-001 – REQ-PLT-005 | api-gateway | Critical |

---

# 6. End-to-End Process Overview

The following process view is provided for orientation. Section 4 remains authoritative for testable behaviour.

```text
Intent Registration
        → Applicant Authentication (OTP + JWT)
        → Multi-step Application + Declaration (Submitted)
        → Fee Payment → Payment Proof → CCA Verification
        → Scrutiny (Edit / Accept / Recommend Audit / Reject)
        → Audit Agency Selection → Pre-Licence Audit + NC Cycle
        → Recommend In-Principle → CCA Grant
        → Bank Guarantee / Supporting Documents
        → Licence Issued (ROLE_LICENSEE)
        → Renewal | Annual Audit | ESP Go-Live | Cessation
```

---

# 7. Glossary

| Term | Definition |
|------|------------|
| CA | Certifying Authority licensed under the IT Act, 2000 |
| CCA | Controller of Certifying Authorities |
| CCALMS | CCA License Management System |
| ESP | eSign Service Provider |
| NC | Non-conformity raised during audit |
| DLT | Distributed Ledger Template framework for TRAI-compliant SMS |
| MSDG | Mobile Seva Delivery Gateway |
| JWT | JSON Web Token used for API authentication |
| In-Principle Approval | Conditional regulatory approval prior to final licence issue |
| Shall | Mandatory requirement verb under ISO/IEC/IEEE 29148 |
| Baseline | Approved requirements set against which delivery and change are measured |

---

# 8. Acceptance and Sign-Off

Acceptance of this document establishes Version 2.0 as the functional baseline for design, build, and test. Subsequent changes shall be managed through formal change control with version increment and re-approval as required by project governance.

| Role | Name | Signature | Date |
|------|------|-----------|------|
| Business Owner (CCA) | | | |
| Delivery / Design Lead | | | |
| Engineering Lead | | | |
| QA / Test Lead | | | |

---

**Document End — FRS-CCALMS-001 / Version 2.0**
