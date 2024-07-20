/** @type {import('ts-jest').JestConfigWithTsJest} */
module.exports = {
  preset: 'ts-jest',
  moduleFileExtensions: ['ts', 'tsx', 'js', 'jsx', 'json', 'node'],
  modulePaths: ['<rootDir>'],
  testEnvironment: 'jsdom',
  reporters: [ "default", "jest-junit" ],
  moduleNameMapper: {
    '^axios$': require.resolve('axios'),
  }
};