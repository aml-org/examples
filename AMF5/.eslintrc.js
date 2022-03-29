module.exports = {
  parser: '@typescript-eslint/parser',
  ignorePatterns: ['*.js', '*.yaml', 'node_modules', 'lib'],
  parserOptions: {
    project: ['./tsconfig.json']
  },
  extends: ['eslint:recommended', 'plugin:@typescript-eslint/recommended', 'prettier'],
  plugins: ['@typescript-eslint'],
  rules: {
    '@typescript-eslint/ban-ts-comment': 'off',
    '@typescript-eslint/no-var-requires': 0,
    '@typescript-eslint/explicit-module-boundary-types': 0
  }
};
